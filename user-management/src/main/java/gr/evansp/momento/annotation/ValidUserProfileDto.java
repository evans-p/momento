package gr.evansp.momento.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import gr.evansp.momento.validator.UserProfileDtoValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static gr.evansp.momento.constant.ValidationConstants.INVALID_INPUT;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserProfileDtoValidator.class)
@Documented
public @interface ValidUserProfileDto {
  String message() default INVALID_INPUT;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
