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
   * FIRST NAME CANNOT BE EMPTY.
   */
  public static final String FIRST_NAME_CANNOT_BE_EMPTY = "{first.name.cannot.be.empty}";

  /**
   * FIRST NAME TOO LONG.
   */
  public static final String FIRST_NAME_TOO_LONG = "{first.name.too.long}";

  /**
   * FIRST NAME CONTAINS INVALID CHARACTERS.
   */
  public static final String FIRST_NAME_CONTAINS_INVALID_CHARACTERS =
      "{first.name.contains.invalid.characters}";

  /**
   * FIRST NAME CANNOT BE EMPTY.
   */
  public static final String LAST_NAME_CANNOT_BE_EMPTY = "{last.name.cannot.be.empty}";

  /**
   * FIRST NAME TOO LONG.
   */
  public static final String LAST_NAME_TOO_LONG = "{last.name.too.long}";

  /**
   * FIRST NAME CONTAINS INVALID CHARACTERS.
   */
  public static final String LAST_NAME_CONTAINS_INVALID_CHARACTERS =
      "{last.name.contains.invalid.characters}";

  /**
   * Private NoArgs Constructor.
   */
  private ValidationConstants() {
    // EMPTY
  }
}
