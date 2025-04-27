package gr.evansp.momento.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gr.evansp.momento.annotation.ValidUserId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static gr.evansp.momento.constant.ExceptionConstants.INVALID_USER_ID;

public class UserIdValidator implements ConstraintValidator<ValidUserId, String> {

	private static final String USER_ID_REGEX =
			"^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}$";

	@Override
	public void initialize(ValidUserId constraintAnnotation) {
		//EMPTY
	}

	@Override
	public boolean isValid(String userId, ConstraintValidatorContext context) {
		boolean isValid = true;

		if (userId == null || userId.isBlank()) {
			isValid = buildConstraintViolationMessage(INVALID_USER_ID, context);
		} else {
			Pattern pattern = Pattern.compile(USER_ID_REGEX);
			Matcher matcher = pattern.matcher(userId);

			if (!matcher.matches()) {
				isValid = buildConstraintViolationMessage(INVALID_USER_ID, context);
			}
		}
		return isValid;
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
