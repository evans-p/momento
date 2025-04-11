package gr.evansp.momento.constants;

import jakarta.validation.ConstraintViolationException;

/**
 * Message code for {@link ConstraintViolationException}.
 */
public class ValidationConstants {

  /**
   * INVALID_FILE.
   */
  public static final String INVALID_FILE = "{invalid.file}";

  /**
   * FAULTY FILE NAME.
   */
  public static final String FAULTY_FILE_NAME = "{faulty.file.name}";

  /**
   * EMPTY_FILE.
   */
  public static final String EMPTY_FILE = "{empty.file}";

  /**
   * INVALID CONTENT TYPE.
   */
  public static final String INVALID_CONTENT_TYPE = "{invalid.content.type}";

  /**
   * TYPE MISMATCH.
   */
  public static final String TYPE_MISMATCH = "{type.mismatch}";

  /**
   * TYPE MISMATCH.
   */
  public static final String INVALID_FILE_CONTENT = "{invalid.file.content}";

  /**
   * Private NoArgs Constructor.
   */
  private ValidationConstants() {
    // EMPTY
  }
}
