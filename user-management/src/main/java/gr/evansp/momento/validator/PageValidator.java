package gr.evansp.momento.validator;

import gr.evansp.momento.annotation.ValidPage;
import gr.evansp.momento.constant.ValidationConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PageValidator implements ConstraintValidator<ValidPage, Integer> {
  @Override
  public void initialize(ValidPage constraintAnnotation) {
    // EMPTY
  }

  @Override
  public boolean isValid(Integer page, ConstraintValidatorContext context) {
    if (page == null || page < 0) {
      return buildConstraintViolationMessage(ValidationConstants.INVALID_PAGE, context);
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
