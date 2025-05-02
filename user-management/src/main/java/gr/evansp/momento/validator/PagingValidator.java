package gr.evansp.momento.validator;

import gr.evansp.momento.annotation.ValidPaging;
import gr.evansp.momento.constant.ValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PagingValidator implements ConstraintValidator<ValidPaging, Integer> {
  @Override
  public void initialize(ValidPaging constraintAnnotation) {
    // EMPTY
  }

  @Override
  public boolean isValid(Integer paging, ConstraintValidatorContext constraintValidatorContext) {
    if (paging == null || paging <= 0 || paging > 30) {
      return buildConstraintViolationMessage(
          ValidationConstants.INVALID_PAGING, constraintValidatorContext);
    }

    return true;
  }

  /**
   * Build validation message.
   *
   * @param messageCode
   * 		messageCode
   * @param context
   * 		context
   * @return false
   */
  private boolean buildConstraintViolationMessage(
      String messageCode, ConstraintValidatorContext context) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(messageCode).addConstraintViolation();

    return false;
  }
}
