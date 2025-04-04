package gr.evansp.momento.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import gr.evansp.momento.validator.FileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.multipart.MultipartFile;

/**
 * Annotation to validate {@link MultipartFile}.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
@Documented
public @interface ValidFile {
	String message() default "{invalid.file}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
