package gr.evansp.momento.annotation;

import static gr.evansp.momento.constant.ValidationConstants.INVALID_INPUT;

import gr.evansp.momento.validator.UserProfileDtoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserProfileDtoValidator.class)
@Documented
public @interface ValidUpdateUserProfileDto {
  String message() default INVALID_INPUT;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
