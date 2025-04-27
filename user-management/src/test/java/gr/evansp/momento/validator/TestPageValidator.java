package gr.evansp.momento.validator;

import java.util.Set;

import gr.evansp.momento.AbstractUnitTest;
import gr.evansp.momento.annotation.ValidPage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PageValidator}.
 */
class TestPageValidator extends AbstractUnitTest {

	@Test
	public void testIsValid_nullPage() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			PageWrapper wrapper = new PageWrapper(null);

			Set<ConstraintViolation<PageWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());

			assertEquals(
					VALIDATION_MESSAGES.getString("invalid.page"),
					violations.iterator().next().getMessage());
		}
	}


	@Test
	public void testIsValid_negativeValue() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			PageWrapper wrapper = new PageWrapper(-1);

			Set<ConstraintViolation<PageWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());

			assertEquals(
					VALIDATION_MESSAGES.getString("invalid.page"),
					violations.iterator().next().getMessage());
		}
	}

	@Test
	public void testIsValid_ok1() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			PageWrapper wrapper = new PageWrapper(0);

			Set<ConstraintViolation<PageWrapper>> violations = validator.validate(wrapper);

			assertEquals(0, violations.size());
		}
	}

	@Test
	public void testIsValid_ok2() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			PageWrapper wrapper = new PageWrapper(0);

			Set<ConstraintViolation<PageWrapper>> violations = validator.validate(wrapper);

			assertEquals(0, violations.size());
		}
	}


	@Setter
	@Getter
	private static class PageWrapper {

		@ValidPage private Integer page;

		public PageWrapper(Integer page) {
			this.page = page;
		}
	}
}