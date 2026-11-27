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

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaParameter;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.MappedSuperclass;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Architecture tests for a Spring Boot 3 application (package-by-layer style).
 *
 * <h3>Dependencies (test scope)</h3>
 *
 * <pre>
 * &lt;dependency&gt;
 *   &lt;groupId&gt;com.tngtech.archunit&lt;/groupId&gt;
 *   &lt;artifactId&gt;archunit-junit5&lt;/artifactId&gt;
 *   &lt;version&gt;1.3.0&lt;/version&gt;
 *   &lt;scope&gt;test&lt;/scope&gt;
 * &lt;/dependency&gt;
 * </pre>
 *
 * <h3>Setup</h3>
 *
 * <ul>
 *   <li>Adjust {@link #BASE_PACKAGE} and the package constants below to your project.
 *   <li>Put the accompanying archunit.properties in src/test/resources so that rules whose subject
 *       set is empty (e.g. you have no scheduled jobs) do not fail.
 *   <li>Rules that are opinionated (marked OPTIONAL) can simply be deleted.
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
  private static final String REPOSITORY = "..repository..";
  private static final String CONFIG = "..configuration..";
  private static final String DTO = "..dto..";
  private static final String EXCEPTION = "..exception..";
  private static final String[] MODEL_PACKAGES = {"..entity..", "..domain..", "..model.."};
  private static final String[] SCHEDULER_PACKAGES = {"..scheduler..", "..job..", "..task.."};

  // =========================================================================================
  // 1. LAYERED ARCHITECTURE & DEPENDENCY DIRECTION
  // =========================================================================================

  @ArchTest
  static final ArchRule layered_architecture_is_respected =
      layeredArchitecture()
          .consideringOnlyDependenciesInLayers()
          .layer("Controller")
          .definedBy(CONTROLLER)
          .layer("Service")
          .definedBy(SERVICE)
          .layer("Repository")
          .definedBy(REPOSITORY)
          .whereLayer("Controller")
          .mayNotBeAccessedByAnyLayer()
          .whereLayer("Service")
          .mayOnlyBeAccessedByLayers("Controller")
          .whereLayer("Repository")
          .mayOnlyBeAccessedByLayers("Service")
          .because("dependencies must flow Controller -> Service -> Repository only");

  @ArchTest
  static final ArchRule model_does_not_depend_on_upper_layers =
      noClasses()
          .that()
          .resideInAnyPackage(MODEL_PACKAGES)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(CONTROLLER, SERVICE, REPOSITORY, DTO)
          .because("the domain model must be independent of the layers that use it");

  @ArchTest
  static final ArchRule controllers_do_not_access_persistence_directly =
      noClasses()
          .that()
          .resideInAPackage(CONTROLLER)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(
              REPOSITORY, "jakarta.persistence..", "org.springframework.data..", "org.hibernate..")
          .because("controllers must go through the service layer");

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
          .because(
              "services must stay independent of the web/HTTP layer "
                  + "(org.springframework.web.multipart is allowed: MultipartFile is a data type)");

  @ArchTest
  static final ArchRule repositories_do_not_depend_on_web_or_services =
      noClasses()
          .that()
          .resideInAPackage(REPOSITORY)
          .should()
          .dependOnClassesThat()
          .resideInAnyPackage(SERVICE, CONTROLLER, "jakarta.servlet..", "org.springframework.web..")
          .because("the persistence layer must not know about upper layers");

  @ArchTest
  static final ArchRule dtos_do_not_hold_entities =
      fields()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage(DTO)
          .should(notHoldEntities())
          .because(
              "DTOs must not carry persistence entities as state "
                  + "(static factories such as AssetDto.of(Asset) are allowed)");

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
  static final ArchRule repositories_are_named_correctly =
      classes()
          .that()
          .areAssignableTo(Repository.class)
          .and()
          .areInterfaces()
          .should()
          .haveSimpleNameEndingWith("Repository");

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
          .because("OPTIONAL: adapt the suffixes to your convention (Command, Query, View, ...)");

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
  static final ArchRule repositories_reside_in_repository_package =
      classes()
          .that()
          .areAssignableTo(Repository.class)
          .or()
          .areAnnotatedWith(org.springframework.stereotype.Repository.class)
          .should()
          .resideInAPackage(REPOSITORY);

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
  static final ArchRule entities_reside_in_model_packages =
      classes()
          .that()
          .areAnnotatedWith(Entity.class)
          .or()
          .areAnnotatedWith(MappedSuperclass.class)
          .or()
          .areAnnotatedWith(Embeddable.class)
          .should()
          .resideInAnyPackage(MODEL_PACKAGES);

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
  static final ArchRule spring_data_repositories_are_interfaces =
      classes()
          .that()
          .areAssignableTo(Repository.class)
          .and()
          .areNotAnnotatedWith(org.springframework.stereotype.Repository.class)
          .should()
          .beInterfaces();

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
    org.junit.jupiter.api.Assertions.assertFalse(
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
  static final ArchRule entity_manager_is_only_used_in_persistence_layer =
      noClasses()
          .that()
          .resideOutsideOfPackages(REPOSITORY, CONFIG)
          .should()
          .dependOnClassesThat()
          .areAssignableTo(EntityManager.class);

  @ArchTest
  static final ArchRule jdbc_template_is_only_used_in_persistence_layer =
      noClasses()
          .that()
          .resideOutsideOfPackages(REPOSITORY, CONFIG)
          .should()
          .dependOnClassesThat()
          .areAssignableTo("org.springframework.jdbc.core.JdbcTemplate")
          .because("raw SQL access belongs in the persistence layer");

  // =========================================================================================
  // 5. TRANSACTIONS
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
  static final ArchRule controller_methods_are_not_transactional =
      noMethods()
          .that()
          .areDeclaredInClassesThat()
          .resideInAPackage(CONTROLLER)
          .should()
          .beAnnotatedWith(Transactional.class)
          .orShould()
          .beAnnotatedWith("jakarta.transaction.Transactional");

  @ArchTest
  static final ArchRule transactional_methods_are_not_private =
      noMethods()
          .that()
          .areAnnotatedWith(Transactional.class)
          .should()
          .bePrivate()
          .because("proxy-based @Transactional is silently ignored on private methods");

  // =========================================================================================
  // 6. DEPENDENCY INJECTION & BEAN DESIGN
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
  // 7. API DESIGN (CONTROLLERS)
  // =========================================================================================

  @ArchTest
  static final ArchRule controllers_do_not_expose_entities =
      methods()
          .that()
          .areDeclaredInClassesThat(annotatedOrMeta(Controller.class))
          .and()
          .arePublic()
          .should(notExposeEntitiesInSignature())
          .because("expose DTOs, not persistence entities, through the API");

  @ArchTest
  static final ArchRule request_bodies_are_validated =
      methods()
          .that()
          .areDeclaredInClassesThat(annotatedOrMeta(Controller.class))
          .and()
          .arePublic()
          .should(validateRequestBodies())
          .because("OPTIONAL: every @RequestBody should be annotated with @Valid/@Validated");

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

  /**
   * Matches classes (or methods' declaring classes) annotated directly or via a meta-annotation.
   */
  private static DescribedPredicate<CanBeAnnotated> annotatedOrMeta(
      Class<? extends Annotation> annotation) {
    return CanBeAnnotated.Predicates.annotatedWith(annotation)
        .or(CanBeAnnotated.Predicates.metaAnnotatedWith(annotation));
  }

  /**
   * Fails if a method's return type or parameter types (including generics) involve a JPA entity.
   */
  private static ArchCondition<JavaMethod> notExposeEntitiesInSignature() {
    return new ArchCondition<>("not expose JPA entities in their signature") {
      @Override
      public void check(JavaMethod method, ConditionEvents events) {
        Stream.concat(
                method.getReturnType().getAllInvolvedRawTypes().stream(),
                method.getParameterTypes().stream()
                    .flatMap(type -> type.getAllInvolvedRawTypes().stream()))
            .filter(ArchitectureTest::isEntityLike)
            .distinct()
            .forEach(
                type ->
                    events.add(
                        SimpleConditionEvent.violated(
                            method, method.getFullName() + " exposes entity " + type.getName())));
      }
    };
  }

  /** Fails if a field's type (including generic arguments) involves a JPA entity. */
  private static ArchCondition<JavaField> notHoldEntities() {
    return new ArchCondition<>("not hold JPA entities") {
      @Override
      public void check(JavaField field, ConditionEvents events) {
        field.getType().getAllInvolvedRawTypes().stream()
            .filter(ArchitectureTest::isEntityLike)
            .distinct()
            .forEach(
                type ->
                    events.add(
                        SimpleConditionEvent.violated(
                            field, field.getFullName() + " holds entity " + type.getName())));
      }
    };
  }

  private static boolean isEntityLike(JavaClass type) {
    return type.isAnnotatedWith(Entity.class)
        || type.isAnnotatedWith(MappedSuperclass.class)
        || type.isAnnotatedWith(Embeddable.class);
  }

  /** Fails if a @RequestBody parameter is not annotated with @Valid or @Validated. */
  private static ArchCondition<JavaMethod> validateRequestBodies() {
    return new ArchCondition<>("validate every @RequestBody parameter") {
      @Override
      public void check(JavaMethod method, ConditionEvents events) {
        for (JavaParameter parameter : method.getParameters()) {
          boolean isBody = parameter.isAnnotatedWith(RequestBody.class);
          boolean validated =
              parameter.isAnnotatedWith("jakarta.validation.Valid")
                  || parameter.isAnnotatedWith(
                      "org.springframework.validation.annotation.Validated");
          if (isBody && !validated) {
            events.add(
                SimpleConditionEvent.violated(
                    method,
                    method.getFullName()
                        + " has a @RequestBody parameter without @Valid/@Validated"));
          }
        }
      }
    };
  }
}
