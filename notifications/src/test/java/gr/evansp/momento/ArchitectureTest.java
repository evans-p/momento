package gr.evansp.momento;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;
import static com.tngtech.archunit.library.GeneralCodingRules.NO_CLASSES_SHOULD_USE_JODATIME;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Architecture tests for the notifications module (Spring Boot 3, web + Kafka + Avro, no
 * persistence).
 *
 * <ul>
 *   <li>Set {@link #BASE_PACKAGE} to this module's root package (the one holding the
 *       {@code @SpringBootApplication} class).
 *   <li>Copy archunit.properties to this module's src/test/resources.
 *   <li>Rules marked OPTIONAL are opinionated and can be deleted.
 * </ul>
 */
@AnalyzeClasses(
    packages = ArchitectureTest.BASE_PACKAGE,
    importOptions = {ImportOption.DoNotIncludeTests.class, ImportOption.DoNotIncludeJars.class})
class ArchitectureTest {

  static final String BASE_PACKAGE = "gr.evansp.momento";

  // ---- Package identifiers (customise to your project) -------------------------------------
  private static final String CONTROLLER = "..controller..";
  private static final String SERVICE = "..service..";
  private static final String CONFIG = "..configuration..";
  private static final String DTO = "..dto..";
  private static final String EXCEPTION = "..exception..";
  private static final String[] MODEL_PACKAGES = {"..entity..", "..domain..", "..model.."};
  private static final String[] SCHEDULER_PACKAGES = {"..scheduler..", "..job..", "..task.."};
  private static final String[] MESSAGING_LIBS = {
    "org.springframework.kafka..", "org.apache.kafka..", "io.confluent..", "org.apache.avro.."
  };

  // =========================================================================================
  // 1. LAYERED ARCHITECTURE & DEPENDENCY DIRECTION
  // =========================================================================================

  @ArchTest
  static final ArchRule layered_architecture_is_respected =
      layeredArchitecture()
          .consideringOnlyDependenciesInLayers()
          .withOptionalLayers(true)
          .layer("Controller")
          .definedBy(CONTROLLER)
          .layer("Service")
          .definedBy(SERVICE)
          .whereLayer("Controller")
          .mayNotBeAccessedByAnyLayer()
          .whereLayer("Service")
          .mayOnlyBeAccessedByLayers("Controller")
          .because("dependencies must flow Controller -> Service only");

  @ArchTest
  static final ArchRule model_does_not_depend_on_upper_layers =
      noClasses()
          .that()
          .resideInAnyPackage(MODEL_PACKAGES)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(CONTROLLER, SERVICE, DTO)
          .because("the domain model must be independent of the layers that use it");

  @ArchTest
  static final ArchRule services_do_not_depend_on_web_layer =
      noClasses()
          .that()
          .resideInAPackage(SERVICE)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              CONTROLLER,
              "jakarta.servlet..",
              "org.springframework.web.bind..",
              "org.springframework.web.servlet..",
              "org.springframework.http..")
          .because("services must stay independent of the web/HTTP layer");

  @ArchTest
  static final ArchRule no_package_cycles =
      slices()
          .matching(BASE_PACKAGE + ".(*)..")
          .should()
          .beFreeOfCycles()
          // Bean Validation is inherently circular: @Constraint(validatedBy = X) <->
          // ConstraintValidator<A, T>
          .ignoreDependency(
              resideInAnyPackage("..annotation.."), resideInAnyPackage("..validator.."));

  // =========================================================================================
  // 2. NAMING CONVENTIONS
  // =========================================================================================

  @ArchTest
  static final ArchRule controllers_are_named_correctly =
      classes()
          .that(annotatedOrMeta(Controller.class)) // includes @RestController
          .and()
          .areTopLevelClasses()
          .and()
          .areNotAnnotatedWith(ControllerAdvice.class)
          .and()
          .areNotMetaAnnotatedWith(ControllerAdvice.class)
          .should()
          .haveSimpleNameEndingWith("Controller");

  @ArchTest
  static final ArchRule services_are_named_correctly =
      classes()
          .that()
          .areAnnotatedWith(Service.class)
          .should()
          .haveNameMatching(".*Service(Impl)?");

  @ArchTest
  static final ArchRule configuration_classes_are_named_correctly =
      classes()
          .that()
          .areAnnotatedWith(Configuration.class)
          .should()
          .haveNameMatching(".*(Config|Configuration)");

  @ArchTest
  static final ArchRule configuration_properties_are_named_correctly =
      classes()
          .that()
          .areAnnotatedWith(ConfigurationProperties.class)
          .should()
          .haveSimpleNameEndingWith("Properties");

  @ArchTest
  static final ArchRule exceptions_are_named_correctly =
      classes()
          .that()
          .areAssignableTo(Throwable.class)
          .and()
          .areTopLevelClasses()
          .should()
          .haveSimpleNameEndingWith("Exception");

  @ArchTest
  static final ArchRule dtos_are_named_correctly =
      classes()
          .that()
          .resideInAPackage(DTO)
          .and()
          .areTopLevelClasses()
          .and()
          .areNotEnums()
          .and()
          .areNotInterfaces()
          .should()
          .haveNameMatching(".*(Dto|Request|Response)")
          .because("OPTIONAL: adapt the suffixes to your convention");

  // =========================================================================================
  // 3. STEREOTYPES BELONG IN THE RIGHT PACKAGES
  // =========================================================================================

  @ArchTest
  static final ArchRule controllers_reside_in_controller_package =
      classes()
          .that(annotatedOrMeta(Controller.class))
          .and()
          .areNotAnnotatedWith(ControllerAdvice.class)
          .and()
          .areNotMetaAnnotatedWith(ControllerAdvice.class)
          .should()
          .resideInAPackage(CONTROLLER);

  @ArchTest
  static final ArchRule controller_advice_resides_in_controller_or_exception_package =
      classes()
          .that(annotatedOrMeta(ControllerAdvice.class)) // includes @RestControllerAdvice
          .should()
          .resideInAnyPackage(CONTROLLER, EXCEPTION);

  @ArchTest
  static final ArchRule services_reside_in_service_package =
      classes().that().areAnnotatedWith(Service.class).should().resideInAPackage(SERVICE);

  @ArchTest
  static final ArchRule configuration_classes_reside_in_config_package =
      classes()
          .that()
          .areAnnotatedWith(Configuration.class)
          .should()
          .resideInAnyPackage(CONFIG, BASE_PACKAGE)
          .because("@SpringBootApplication (root package) is itself a @Configuration");

  @ArchTest
  static final ArchRule configuration_properties_reside_in_config_package =
      classes()
          .that()
          .areAnnotatedWith(ConfigurationProperties.class)
          .should()
          .resideInAnyPackage(CONFIG, "..properties..");

  @ArchTest
  static final ArchRule exceptions_reside_in_exception_package =
      classes()
          .that()
          .areAssignableTo(Throwable.class)
          .and()
          .areTopLevelClasses()
          .should()
          .resideInAPackage(EXCEPTION);

  @ArchTest
  static final ArchRule custom_exceptions_are_unchecked =
      classes()
          .that()
          .resideInAPackage(EXCEPTION)
          .and()
          .areAssignableTo(Throwable.class)
          .should()
          .beAssignableTo(RuntimeException.class)
          .because("Spring's transaction and error handling is built around unchecked exceptions");

  @ArchTest
  static final ArchRule dtos_are_records =
      classes()
          .that()
          .resideInAPackage(DTO)
          .and()
          .areTopLevelClasses()
          .and()
          .areNotEnums()
          .and()
          .areNotInterfaces()
          .should()
          .beAssignableTo(Record.class)
          .because("OPTIONAL: immutable DTOs are easiest to model as records");

  // =========================================================================================
  // 4. SPRING BOOT SPECIFICS
  // =========================================================================================

  @ArchTest
  static final ArchRule spring_boot_application_is_in_root_package =
      classes()
          .that()
          .areAnnotatedWith(SpringBootApplication.class)
          .should()
          .resideInAPackage(BASE_PACKAGE)
          .because("component scanning starts from the package of the application class");

  @ArchTest
  static void base_package_contains_classes(JavaClasses classes) {
    assertFalse(
        classes.isEmpty(), "No classes imported from " + BASE_PACKAGE + " - check BASE_PACKAGE");
  }

  @ArchTest
  static void there_is_exactly_one_spring_boot_application(JavaClasses classes) {
    long count =
        classes.stream().filter(c -> c.isAnnotatedWith(SpringBootApplication.class)).count();
    assertEquals(1, count, "Expected exactly one @SpringBootApplication class");
  }

  @ArchTest
  static final ArchRule nobody_depends_on_the_application_class =
      noClasses().should().dependOnClassesThat().areAnnotatedWith(SpringBootApplication.class);

  @ArchTest
  static final ArchRule bean_methods_are_declared_in_configuration_classes =
      methods()
          .that()
          .areAnnotatedWith(Bean.class)
          .should()
          .beDeclaredInClassesThat(annotatedOrMeta(Configuration.class))
          .because("@Bean factory methods belong in @Configuration classes (full mode)");

  @ArchTest
  static final ArchRule request_mapping_methods_are_in_controllers =
      methods()
          .that()
          .areAnnotatedWith(RequestMapping.class)
          .or()
          .areMetaAnnotatedWith(RequestMapping.class) // @GetMapping, @PostMapping, ...
          .should()
          .beDeclaredInClassesThat(annotatedOrMeta(Controller.class));

  @ArchTest
  static final ArchRule scheduled_methods_are_in_scheduler_packages =
      methods()
          .that()
          .areAnnotatedWith(Scheduled.class)
          .should()
          .beDeclaredInClassesThat()
          .resideInAnyPackage(SCHEDULER_PACKAGES)
          .because("OPTIONAL: keep scheduled jobs in a dedicated package");

  @ArchTest
  static final ArchRule caching_annotations_are_only_used_in_services =
      methods()
          .that()
          .areAnnotatedWith(Cacheable.class)
          .or()
          .areAnnotatedWith(CacheEvict.class)
          .or()
          .areAnnotatedWith(CachePut.class)
          .should()
          .beDeclaredInClassesThat()
          .resideInAPackage(SERVICE);

  @ArchTest
  static final ArchRule no_devtools_in_production_code =
      noClasses()
          .should()
          .dependOnClassesThat()
          .resideInAPackage("org.springframework.boot.devtools..");

  // =========================================================================================
  // 5. KAFKA / MESSAGING
  // =========================================================================================

  @ArchTest
  static final ArchRule kafka_listeners_are_declared_in_spring_beans =
      methods()
          .that()
          .areAnnotatedWith(KafkaListener.class)
          .should()
          .beDeclaredInClassesThat(annotatedOrMeta(Component.class))
          .because("@KafkaListener is silently ignored on classes that are not Spring beans");

  @ArchTest
  static final ArchRule kafka_listeners_are_not_in_controllers =
      noMethods()
          .that()
          .areAnnotatedWith(KafkaListener.class)
          .should()
          .beDeclaredInClassesThat()
          .resideInAPackage(CONTROLLER)
          .because("message consumption is not part of the HTTP layer");

  @ArchTest
  static final ArchRule controllers_do_not_use_messaging_infrastructure =
      noClasses()
          .that()
          .resideInAPackage(CONTROLLER)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(MESSAGING_LIBS)
          .because("OPTIONAL: controllers should delegate messaging to services");

  // =========================================================================================
  // 6. TRANSACTIONS
  // =========================================================================================

  @ArchTest
  static final ArchRule controllers_are_not_transactional =
      noClasses()
          .that()
          .resideInAPackage(CONTROLLER)
          .should()
          .beAnnotatedWith(Transactional.class)
          .orShould()
          .beAnnotatedWith("jakarta.transaction.Transactional")
          .because("transaction boundaries belong in the service layer");

  @ArchTest
  static final ArchRule transactional_methods_are_not_private =
      noMethods()
          .that()
          .areAnnotatedWith(Transactional.class)
          .should()
          .bePrivate()
          .because("proxy-based @Transactional is silently ignored on private methods");

  // =========================================================================================
  // 7. DEPENDENCY INJECTION & BEAN DESIGN
  // =========================================================================================

  @ArchTest static final ArchRule no_field_injection = NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  static final ArchRule no_setter_injection =
      noMethods()
          .should()
          .beAnnotatedWith(Autowired.class)
          .because("use constructor injection: it makes dependencies explicit and testable");

  @ArchTest
  static final ArchRule stateless_beans_have_only_private_final_fields =
      fields()
          .that()
          .areDeclaredInClassesThat(
              annotatedOrMeta(Controller.class).or(annotatedOrMeta(Service.class)))
          .and()
          .areNotStatic()
          .should()
          .bePrivate()
          .andShould()
          .beFinal()
          .because("singleton beans must be stateless to be thread-safe");

  // =========================================================================================
  // 8. GENERAL CODING RULES
  // =========================================================================================

  @ArchTest static final ArchRule no_standard_streams = NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

  @ArchTest
  static final ArchRule no_generic_exceptions = NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

  @ArchTest static final ArchRule no_java_util_logging = NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING;

  @ArchTest static final ArchRule no_joda_time = NO_CLASSES_SHOULD_USE_JODATIME;

  @ArchTest
  static final ArchRule no_print_stack_trace =
      noClasses()
          .should()
          .callMethod(Throwable.class, "printStackTrace")
          .because("use the logger instead");

  @ArchTest
  static final ArchRule loggers_are_private_static_final =
      fields()
          .that()
          .haveRawType(Logger.class)
          .should()
          .bePrivate()
          .andShould()
          .beStatic()
          .andShould()
          .beFinal();

  @ArchTest
  static final ArchRule no_mutable_static_fields =
      fields()
          .that()
          .areStatic()
          .should()
          .beFinal()
          .because("mutable static state breaks thread-safety and testability");

  @ArchTest
  static final ArchRule no_legacy_date_api =
      noClasses()
          .should()
          .dependOnClassesThat()
          .belongToAnyOf(Date.class, Calendar.class, SimpleDateFormat.class)
          .because("OPTIONAL: prefer java.time");

  @ArchTest
  static final ArchRule no_deprecated_api_usage =
      noClasses()
          .should()
          .dependOnClassesThat()
          .areAnnotatedWith(Deprecated.class)
          .because("OPTIONAL: avoid APIs that are scheduled for removal");

  @ArchTest
  static final ArchRule no_test_libraries_in_production_code =
      noClasses()
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "org.junit..",
              "org.mockito..",
              "org.assertj..",
              "org.hamcrest..",
              "org.springframework.test..");

  // =========================================================================================
  // 9. SPRING BOOT 3 MIGRATION GUARD (javax -> jakarta)
  // =========================================================================================

  @ArchTest
  static final ArchRule no_javax_ee_packages =
      noClasses()
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              "javax.persistence..",
              "javax.servlet..",
              "javax.validation..",
              "javax.annotation..",
              "javax.inject..",
              "javax.transaction..",
              "javax.ws.rs..")
          .because("Spring Boot 3 / Jakarta EE 10 uses the jakarta.* namespace");

  // =========================================================================================
  // Helpers
  // =========================================================================================

  /** Matches classes annotated directly or via a meta-annotation. */
  private static DescribedPredicate<CanBeAnnotated> annotatedOrMeta(
      Class<? extends Annotation> annotation) {
    return CanBeAnnotated.Predicates.annotatedWith(annotation)
        .or(CanBeAnnotated.Predicates.metaAnnotatedWith(annotation));
  }
}
