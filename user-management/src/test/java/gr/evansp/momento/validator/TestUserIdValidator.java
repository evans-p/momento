package gr.evansp.momento.validator;

import java.util.Set;

import gr.evansp.momento.AbstractUnitTest;
import gr.evansp.momento.annotation.ValidUserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link UserIdValidator}.
 */
class TestUserIdValidator extends AbstractUnitTest {


	@Test
	public void testIsValid_nullUserId() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			UserIdWrapper wrapper = new UserIdWrapper(null);

			Set<ConstraintViolation<UserIdWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(
					VALIDATION_MESSAGES.getString("invalid.user.id"),
					violations.iterator().next().getMessage());
		}
	}

	@Test
	public void testIsValid_emptyUserId() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			UserIdWrapper wrapper = new UserIdWrapper("");

			Set<ConstraintViolation<UserIdWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(
					VALIDATION_MESSAGES.getString("invalid.user.id"),
					violations.iterator().next().getMessage());
		}
	}


	@Test
	public void testIsValid_blankUserId() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			UserIdWrapper wrapper = new UserIdWrapper("    ");

			Set<ConstraintViolation<UserIdWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(
					VALIDATION_MESSAGES.getString("invalid.user.id"),
					violations.iterator().next().getMessage());
		}
	}

	@Test
	public void testIsValid_faultyUserId() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			UserIdWrapper wrapper = new UserIdWrapper("Mullets");

			Set<ConstraintViolation<UserIdWrapper>> violations = validator.validate(wrapper);

			assertEquals(1, violations.size());
			assertEquals(
					VALIDATION_MESSAGES.getString("invalid.user.id"),
					violations.iterator().next().getMessage());
		}
	}


	@Test
	public void testIsValid_ok() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();

			UserIdWrapper wrapper = new UserIdWrapper("6de22d6a-60ea-4fe2-b4a4-eb1ed5fb838b");

			Set<ConstraintViolation<UserIdWrapper>> violations = validator.validate(wrapper);

			assertEquals(0, violations.size());
		}
	}


	@Setter
	@Getter
	private static class UserIdWrapper {

		@ValidUserId private String userId;

		public UserIdWrapper(String userId) {
			this.userId = userId;
		}
	}
}