package gr.evansp.momento.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gr.evansp.momento.AbstractUnitTest;
import gr.evansp.momento.annotation.ValidPaging;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PagingValidator}.
 */
public class TestPagingValidator extends AbstractUnitTest {

  /**
   * Test for {@link PagingValidator#isValid(Integer, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_nullPaging() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      PagingWrapper wrapper = new PagingWrapper(null);

      Set<ConstraintViolation<PagingWrapper>> violations = validator.validate(wrapper);

      assertEquals(1, violations.size());

      assertEquals(
          VALIDATION_MESSAGES.getString("invalid.paging"),
          violations.iterator().next().getMessage());
    }
  }

  /**
   * Test for {@link PagingValidator#isValid(Integer, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_negativePaging() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      PagingWrapper wrapper = new PagingWrapper(-1);

      Set<ConstraintViolation<PagingWrapper>> violations = validator.validate(wrapper);

      assertEquals(1, violations.size());

      assertEquals(
          VALIDATION_MESSAGES.getString("invalid.paging"),
          violations.iterator().next().getMessage());
    }
  }

  /**
   * Test for {@link PagingValidator#isValid(Integer, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_zeroPaging() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      PagingWrapper wrapper = new PagingWrapper(0);

      Set<ConstraintViolation<PagingWrapper>> violations = validator.validate(wrapper);

      assertEquals(1, violations.size());

      assertEquals(
          VALIDATION_MESSAGES.getString("invalid.paging"),
          violations.iterator().next().getMessage());
    }
  }

  /**
   * Test for {@link PagingValidator#isValid(Integer, ConstraintValidatorContext)}.
   */
  @Test
  public void testIsValid_ok() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();

      PagingWrapper wrapper = new PagingWrapper(1);

      Set<ConstraintViolation<PagingWrapper>> violations = validator.validate(wrapper);

      assertEquals(0, violations.size());
    }
  }

  @Setter
  @Getter
  private static class PagingWrapper {

    @ValidPaging private Integer paging;

    public PagingWrapper(Integer paging) {
      this.paging = paging;
    }
  }
}
