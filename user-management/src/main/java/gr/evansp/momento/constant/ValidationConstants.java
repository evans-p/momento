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
   * INVALID INPUT.
   */
  public static final String INVALID_INPUT = "{invalid.input}";

  /**
   * INVALID URL.
   */
  public static final String INVALID_URL = "{invalid.url}";

  /**
   * URL TOO LONG.
   */
  public static final String URL_TOO_LONG = "{url.too.long}";

  /**
   * INVALID DOMAIN ADDRESS.
   */
  public static final String INVALID_DOMAIN_ADDRESS = "{invalid.domain.address}";

  /**
   * FIELD CANNOT BE EMPTY.
   */
  public static final String FIELD_CANNOT_BE_EMPTY = "{field.cannot.be.empty}";

  /**
   * FIELD TOO LONG.
   */
  public static final String FIELD_TOO_LONG = "{field.too.long}";

  /**
   * FIELD CONTAINS INVALID CHARACTERS.
   */
  public static final String FIELD_CONTAINS_INVALID_CHARACTERS =
      "{field.contains.invalid.characters}";

  /**
   * Private NoArgs Constructor.
   */
  private ValidationConstants() {
    // EMPTY
  }
}
