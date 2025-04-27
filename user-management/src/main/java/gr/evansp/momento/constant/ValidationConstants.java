package gr.evansp.momento.constant;

import jakarta.validation.ConstraintViolationException;

/**
 * Message code for {@link ConstraintViolationException}.
 */
public class ValidationConstants {

  /**
   * INVALID USER ID.
   */
  public static final String INVALID_USER_ID = "{invalid.user.id}";

  /**
   * INVALID PAGE.
   */
  public static final String INVALID_PAGE = "{invalid.page}";

  /**
   * INVALID PAGING.
   */
  public static final String INVALID_PAGING = "{invalid.paging}";

  /**
   * Private NoArgs Constructor.
   */
  private ValidationConstants() {
    // EMPTY
  }
}
