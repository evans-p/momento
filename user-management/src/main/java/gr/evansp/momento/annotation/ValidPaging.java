package gr.evansp.momento.annotation;

import static gr.evansp.momento.constant.ValidationConstants.INVALID_PAGING;

import gr.evansp.momento.validator.PagingValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PagingValidator.class)
@Documented
public @interface ValidPaging {
  String message() default INVALID_PAGING;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
