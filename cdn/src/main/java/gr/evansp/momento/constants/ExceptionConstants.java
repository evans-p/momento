package gr.evansp.momento.constants;

/**
 * Message code for {@link Exception}.
 */
public class ExceptionConstants {
  /**
   * FILE NOT FOUND.
   */
  public static final String FILE_NOT_FOUND = "file.not.found";

  /**
   * FAILED TO PROCESS FILE.
   */
  public static final String FILE_PROCESS_FAILED = "file.process.failed";

  /**
   * CANNOT PROCESS REQUEST.
   */
  public static final String CANNOT_PROCESS_REQUEST = "cannot.process.request";

  /**
   * RESOURCE NOT FOUND.
   */
  public static final String RESOURCE_NOT_FOUND = "resource.not.found";

  /**
   * FAULTY MESSAGE BODY.
   */
  public static final String FAULTY_MESSAGE_BODY = "faulty.message.body";

  /**
   * MEDIA TYPE NOT SUPPORTED.
   */
  public static final String MEDIA_TYPE_NOT_SUPPORTED = "media.type.not.supported";

  /**
   * METHOD NOT SUPPORTED.
   */
  public static final String METHOD_NOT_SUPPORTED = "method.not.supported";

  /**
   * INTERNAL SERVER ERROR.
   */
  public static final String INTERNAL_SERVER_ERROR = "internal.server.error";

  /**
   * PAYLOAD TOO LARGE.
   */
  public static final String PAYLOAD_TOO_LARGE = "payload.too.large";

  /**
   * Private NoArgs Constructor.
   */
  private ExceptionConstants() {
    // EMPTY
  }
}
