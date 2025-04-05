package gr.evansp.momento.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gr.evansp.momento.annotation.ValidFileName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileNameValidator implements ConstraintValidator<ValidFileName, String> {

	private static final String REGEX = "^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}-[a-zA-Z0-9]{8}\\.(jpg|jpeg|png)$";


	@Override
	public void initialize(ValidFileName constraintAnnotation) {
		//EMPTY
	}

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		boolean isValid = true;

		if (name == null || name.isBlank()) {
			isValid = buildConstraintViolationMessage("{faulty.file.name}", context);
		} else {
			Pattern pattern = Pattern.compile(REGEX);
			Matcher matcher = pattern.matcher(name);
			if (!matcher.matches()) {
				isValid = buildConstraintViolationMessage("{faulty.file.name}", context);
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
	private boolean buildConstraintViolationMessage(String messageCode, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageCode).addConstraintViolation();

		return false;
	}
}
