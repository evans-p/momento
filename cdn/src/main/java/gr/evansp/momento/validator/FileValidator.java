package gr.evansp.momento.validator;

import gr.evansp.momento.annotation.ValidFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link ConstraintValidator} for {@link MultipartFile}.
 */
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
	@Override
	public void initialize(ValidFile constraintAnnotation) {
	}

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {

		boolean valid = true;

		if (file == null || file.isEmpty()) {
			valid = buildConstraintViolationMessage("{empty.file}", constraintValidatorContext);
		}

		if (file != null && (file.getOriginalFilename() == null ||
				    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).isBlank())) {
			valid = buildConstraintViolationMessage("{faulty.file.name}", constraintValidatorContext);
		}

		return valid;
	}

	private boolean buildConstraintViolationMessage(String messageCode, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageCode).addConstraintViolation();

		return false;
	}
}
