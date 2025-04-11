package gr.evansp.momento.validator;

import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gr.evansp.momento.annotation.ValidFileName;
import gr.evansp.momento.constants.ValidationConstants;
import gr.evansp.momento.util.FileContentTypes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileNameValidator implements ConstraintValidator<ValidFileName, String> {

	private static final String REGEX = getRegex();


	@Override
	public void initialize(ValidFileName constraintAnnotation) {
		//EMPTY
	}

	/**
	 * Builds the Regular expression, to validate file names.
	 *
	 * @return REGEX
	 */
	private static String getRegex() {
		StringBuilder builder = new StringBuilder("^[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}-[a-zA-Z0-9]{8}\\.(");


		FileContentTypes.getValidMedia()
				.stream()
				.map(t -> t.b)
				.flatMap(Set::stream)
				.forEach((type) -> builder.append(type).append("|"));

		builder.deleteCharAt(builder.length()-1);
		return builder.append(")$").toString();
	}

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		boolean isValid = true;

		if (name == null || name.isBlank()) {
			isValid = buildConstraintViolationMessage(ValidationConstants.FAULTY_FILE_NAME, context);
		} else {
			Pattern pattern = Pattern.compile(REGEX);
			Matcher matcher = pattern.matcher(name);
			if (!matcher.matches()) {
				isValid = buildConstraintViolationMessage(ValidationConstants.FAULTY_FILE_NAME, context);
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
