package gr.evansp.momento.validator;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import gr.evansp.momento.annotation.ValidFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.http.MediaType;
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
		VALID_CONTENT_TYPES.put(MediaType.IMAGE_JPEG_VALUE, Set.of("jpg", "jpeg"));
		VALID_CONTENT_TYPES.put(MediaType.IMAGE_PNG_VALUE, Set.of("png"));
	}

	private boolean isValid = true;

	/**
	 * validFileName flag.
	 */
	private boolean validFileName;

	/**
	 * validContentType flag.
	 */
	private boolean validContentType;

	/**
	 * validMatchBetweenFileExtensionAndContentType flag.
	 */
	private boolean validMatchBetweenFileExtensionAndContentType;

	@Override
	public void initialize(ValidFile constraintAnnotation) {
		//EMPTY
	}

	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

		if (file == null || file.isEmpty() || file.getSize() < 4) {
			return buildConstraintViolationMessage("{empty.file}", context);
		}

		validFileName = validateFileName(file, context);
		validContentType = validateContentType(file, context);
		validMatchBetweenFileExtensionAndContentType = validateMatchBetweenFileExtensionAndContentType(file, context);
		validateFileContent(file, context);

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

	/**
	 * Validates file name.
	 *
	 * @param file
	 * 		file
	 * @param context
	 * 		constraintValidatorContext
	 * @return validation check
	 */
	private boolean validateFileName(MultipartFile file, ConstraintValidatorContext context) {
		if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank() || !file.getOriginalFilename().contains(".") || file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1).isBlank()) {
			isValid = buildConstraintViolationMessage("{faulty.file.name}", context);
			return false;
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
	 * @return validation check
	 */
	private boolean validateContentType(MultipartFile file, ConstraintValidatorContext context) {
		if (file.getContentType() == null || !VALID_CONTENT_TYPES.containsKey(file.getContentType())) {
			isValid = buildConstraintViolationMessage("{invalid.content.type}", context);
			return false;
		}
		return true;
	}

	private boolean validateMatchBetweenFileExtensionAndContentType(MultipartFile file, ConstraintValidatorContext context) {
		if ((!validFileName) || (!validContentType)) {
			return true;
		}
		if (!VALID_CONTENT_TYPES.get(file.getContentType()).contains(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1))) {
			isValid = buildConstraintViolationMessage("{type.mismatch}", context);
			return false;
		}
		return true;
	}

	private void validateFileContent(MultipartFile file, ConstraintValidatorContext context) {
		byte[] data;
		try {
			data = file.getBytes();
		} catch (IOException e) {
			isValid = buildConstraintViolationMessage("{invalid.file.content}", context);
			return;
		}

		try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
			BufferedImage image = ImageIO.read(bais);
			if (image == null) {
				isValid = buildConstraintViolationMessage("{invalid.file.content}", context);
				return;
			}
			if (validContentType && validMatchBetweenFileExtensionAndContentType && getContentType(data, file) == null) {
				isValid = buildConstraintViolationMessage("{invalid.file.content}", context);
			}
		} catch (IOException e) {
			isValid = buildConstraintViolationMessage("{invalid.file.content}", context);
		}
	}

	private String getContentType(byte[] imageData, MultipartFile file) {

		if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8 && imageData[2] == (byte) 0xFF) {
			return MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType()) ? MediaType.IMAGE_JPEG_VALUE : null;
		} else if (imageData[0] == (byte) 0x89 && imageData[1] == (byte) 0x50 && imageData[2] == (byte) 0x4E && imageData[3] == (byte) 0x47) {
			return MediaType.IMAGE_PNG_VALUE.equals(file.getContentType()) ? MediaType.IMAGE_PNG_VALUE : null;
		}
		return null;
	}
}
