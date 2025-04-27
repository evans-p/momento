package gr.evansp.momento.constant;

import jakarta.validation.ConstraintViolationException;

/**
 * Message code for {@link ConstraintViolationException}.
 */
public class ValidationConstants {

  /**
   * INVALID_FILE.
   */
  public static final String INVALID_USER_ID = "{invalid.user.id}";

  /**
   * Private NoArgs Constructor.
   */
  private ValidationConstants() {
    // EMPTY
  }
}
