package gr.evansp.momento.validator;

import java.util.Set;

import gr.evansp.momento.AbstractUnitTest;
import gr.evansp.momento.annotation.ValidFileName;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link FileNameValidator}.
 */
class TestFileNameValidator extends AbstractUnitTest {

	/**
	 * Test for {@link FileNameValidator#isValid(String, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_nullFileName() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileNameWrapper wrapper = new FileNameWrapper(null);

			Set<ConstraintViolation<FileNameWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileNameValidator#isValid(String, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_emptyFileName() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileNameWrapper wrapper = new FileNameWrapper("");

			Set<ConstraintViolation<FileNameWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileNameValidator#isValid(String, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_blankFileName() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileNameWrapper wrapper = new FileNameWrapper("     ");

			Set<ConstraintViolation<FileNameWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileNameValidator#isValid(String, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_faultyFileName() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileNameWrapper wrapper = new FileNameWrapper("lalala.jpg");

			Set<ConstraintViolation<FileNameWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(VALIDATION_MESSAGES.getString("faulty.file.name"), violations.iterator().next().getMessage());
		}
	}

	/**
	 * Test for {@link FileNameValidator#isValid(String, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_ok() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileNameWrapper wrapper = new FileNameWrapper("10283bb6-8664-474d-9240-2036b59b4ece-f1e8af53.jpg");

			Set<ConstraintViolation<FileNameWrapper>> violations = validator.validate(wrapper);

			assertEquals(0, violations.size());
		}
	}

	/**
	 * Test for {@link FileNameValidator#isValid(String, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_ok2() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileNameWrapper wrapper = new FileNameWrapper("10283bb6-8664-474d-9240-2036b59b4ece-f1e8af53.jpeg");

			Set<ConstraintViolation<FileNameWrapper>> violations = validator.validate(wrapper);

			assertEquals(0, violations.size());
		}
	}

	/**
	 * Test for {@link FileNameValidator#isValid(String, ConstraintValidatorContext)}.
	 */
	@Test
	public void testIsValid_ok3() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			FileNameWrapper wrapper = new FileNameWrapper("10283bb6-8664-474d-9240-2036b59b4ece-f1e8af53.png");

			Set<ConstraintViolation<FileNameWrapper>> violations = validator.validate(wrapper);

			assertEquals(0, violations.size());
		}
	}


	@Setter
	@Getter
	private static class FileNameWrapper {

		@ValidFileName
		private String fileName;

		public FileNameWrapper(String fileName) {
			this.fileName = fileName;
		}
	}
}