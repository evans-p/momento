package gr.evansp.momento.validator;

import java.util.Set;

import gr.evansp.momento.AbstractUnitTest;
import gr.evansp.momento.annotation.ValidFile;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link FileValidator}.
 */
class TestFileValidator extends AbstractUnitTest {

	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_nullFile() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileWrapper wrapper = new FileWrapper(null);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("empty.file"), violations.iterator().next().getMessage());
		}
	}


	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_emptyFile() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", (byte[]) null); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("empty.file"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_originalNameNull() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", null, "image/jpeg", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}


	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_originalNameEmpty() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "", "image/jpeg", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}


	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_originalNameBlank() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "   ", "image/jpeg", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}


	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_originalNameNoDot() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke", "image/jpeg", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_originalNameNoSuffix() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke.", "image/jpeg", JPG_IMAGE);

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}


	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_nullContentType() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", null, JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("invalid.content.type"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_emptyContentType() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("invalid.content.type"), violations.iterator().next().getMessage());
		}
	}


	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_faultyContentType() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "multipart/mixed", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("invalid.content.type"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_mismatchBetweenFileExtensionAndContentType() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/png", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());

			assertEquals(VALIDATION_MESSAGES.getString("type.mismatch"), violations.iterator().next().getMessage());
		}
	}


	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_ok() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", JPG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertTrue(violations.isEmpty());
		}
	}

	/**
	 * Test for {@link FileValidator#isValid(MultipartFile, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_faultyFileContentType() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			MultipartFile file = new MockMultipartFile("file", "Yosuke.jpg", "image/jpeg", PNG_IMAGE); // 6MB

			FileWrapper wrapper = new FileWrapper(file);

			Set<ConstraintViolation<FileWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("invalid.file.content"), violations.iterator().next().getMessage());
		}
	}


	@Setter
	@Getter
	private static class FileWrapper {

		@ValidFile
		private MultipartFile file;

		public FileWrapper(MultipartFile file) {
			this.file = file;
		}
	}
}