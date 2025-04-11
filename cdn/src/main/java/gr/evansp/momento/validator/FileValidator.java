package gr.evansp.momento.validator;

import gr.evansp.momento.annotation.ValidFile;
import gr.evansp.momento.constants.ValidationConstants;
import gr.evansp.momento.util.FileContentTypes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

/**
 * {@link ConstraintValidator} for {@link MultipartFile}.
 */
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

  @Override
  public void initialize(ValidFile constraintAnnotation) {}

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

    boolean isValid = true;

    if (file == null || file.isEmpty() || file.getSize() < 4) {
      return buildConstraintViolationMessage(ValidationConstants.EMPTY_FILE, context);
    }

    if (file.getOriginalFilename() == null
        || file.getOriginalFilename().isBlank()
        || !file.getOriginalFilename().contains(".")
        || file.getOriginalFilename()
            .substring(file.getOriginalFilename().lastIndexOf(".") + 1)
            .isBlank()) {
      isValid = buildConstraintViolationMessage(ValidationConstants.FAULTY_FILE_NAME, context);
    }

    if (file.getContentType() == null
        || FileContentTypes.getValidMedia().stream()
            .noneMatch(t -> t.a.equals(file.getContentType()))) {
      isValid = buildConstraintViolationMessage(ValidationConstants.INVALID_CONTENT_TYPE, context);
    }

    if (isValid) {
      if (FileContentTypes.getValidMedia().stream()
              .filter(t -> t.a.equals(file.getContentType()))
              .peek(System.out::println)
              .filter(
                  t ->
                      t.b.contains(
                          file.getOriginalFilename()
                              .substring(file.getOriginalFilename().lastIndexOf(".") + 1)))
              .count()
          == 0) {
        isValid = buildConstraintViolationMessage(ValidationConstants.TYPE_MISMATCH, context);
      }
    }

    if (isValid) {
      byte[] data;

      try {
        data = file.getBytes();
      } catch (IOException e) {
        return buildConstraintViolationMessage(ValidationConstants.INVALID_FILE_CONTENT, context);
      }

      try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
        BufferedImage image = ImageIO.read(bais);
        if (image == null) {
          isValid =
              buildConstraintViolationMessage(ValidationConstants.INVALID_FILE_CONTENT, context);
        }

        if (file.getContentType() != null
            && !file.getContentType().equals(FileContentTypes.getContentTypeAsString(data))) {
          isValid =
              buildConstraintViolationMessage(ValidationConstants.INVALID_FILE_CONTENT, context);
        }
      } catch (IOException e) {
        isValid =
            buildConstraintViolationMessage(ValidationConstants.INVALID_FILE_CONTENT, context);
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
