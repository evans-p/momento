package gr.evansp.momento.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import gr.evansp.momento.constant.ValidationConstants;
import gr.evansp.momento.validators.UserIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.multipart.MultipartFile;

/**
 * Annotation to validate {@link MultipartFile}.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
@Documented
public @interface ValidUserId {

	String message() default ValidationConstants.INVALID_USER_ID;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
