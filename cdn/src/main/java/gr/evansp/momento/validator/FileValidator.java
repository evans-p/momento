package gr.evansp.momento.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import gr.evansp.momento.annotation.ValidFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link ConstraintValidator} for {@link MultipartFile}.
 */
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {


	/**
	 * Valid Content Types.
	 */
	private static final Map<String, Set<String>> VALID_CONTENT_TYPES = new HashMap<>();

	static {
		VALID_CONTENT_TYPES.put("image/gif", Set.of("gif"));
		VALID_CONTENT_TYPES.put("image/jpeg", Set.of("jpg", "jpeg"));
		VALID_CONTENT_TYPES.put("image/png", Set.of("png"));
		VALID_CONTENT_TYPES.put("image/tiff", Set.of("tif", "tiff"));
		VALID_CONTENT_TYPES.put("image/vnd.microsoft.icon", Set.of("ico"));
		VALID_CONTENT_TYPES.put("image/x-icon", Set.of("ico"));
		VALID_CONTENT_TYPES.put("image/vnd.djvu", Set.of("djvu"));
		VALID_CONTENT_TYPES.put("image/svg+xml", Set.of("svg"));
	}

	@Override
	public void initialize(ValidFile constraintAnnotation) {
		//EMPTY
	}

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

		if (file == null || file.isEmpty()) {
			return buildConstraintViolationMessage("{empty.file}", context);
		}

		return validateFileName(file, context) && validateContentType(file, context) && validateMatchBetweenFileExtensionAndContentType(file, context);
	}


	private boolean validateMatchBetweenFileExtensionAndContentType(MultipartFile file, ConstraintValidatorContext context) {
		if(!VALID_CONTENT_TYPES.get(file.getContentType()).contains(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1))) {
			return buildConstraintViolationMessage("{type.mismatch}", context);
		}
		return true;
	}

	/**
	 * Validates content type.
	 *
	 * @param file
	 * 		file
	 * @param context
	 * 		context
	 * @return valid or not.
	 */
	private boolean validateContentType(MultipartFile file, ConstraintValidatorContext context) {
		if (file.getContentType() == null || !VALID_CONTENT_TYPES.containsKey(file.getContentType())) {
			return buildConstraintViolationMessage("{invalid.content.type}", context);
		}
		return true;
	}

	/**
	 * Validates file name.
	 *
	 * @param file
	 * 		file
	 * @param context
	 * 		constraintValidatorContext
	 * @return valid or not.
	 */
	private boolean validateFileName(MultipartFile file, ConstraintValidatorContext context) {
		if (file.getOriginalFilename() == null ||
				file.getOriginalFilename().isBlank() ||
				!file.getOriginalFilename().contains(".") ||
				file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).isBlank()) {
			return buildConstraintViolationMessage("{faulty.file.name}", context);
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
	private boolean buildConstraintViolationMessage(String messageCode, ConstraintValidatorContext context) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageCode).addConstraintViolation();

		return false;
	}
}
