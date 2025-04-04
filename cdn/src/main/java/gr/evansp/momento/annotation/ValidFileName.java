package gr.evansp.momento.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import gr.evansp.momento.validator.FileNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Annotation to validate file name
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileNameValidator.class)
@Documented
public @interface ValidFileName {
	String message() default "{faulty.file.name}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
