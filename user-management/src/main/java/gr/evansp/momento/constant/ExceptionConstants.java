package gr.evansp.momento.constant;

/**
 * Message code for {@link Exception}.
 */
public class ExceptionConstants {
  /**
   * INVALID TOKEN.
   */
  public static final String INVALID_TOKEN = "invalid.token";

  /**
   * USER ALREADY REGISTERED.
   */
  public static final String USER_ALREADY_REGISTERED = "user.already.registered";

  /**
   * USER NOT FOUND.
   */
  public static final String USER_NOT_FOUND = "user.not.found";

  /**
   * USER ALREADY REGISTERED.
   */
  public static final String EMAIL_ALREADY_REGISTERED = "email.already.registered";

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
   * AUTHORIZATION HEADER NOT PRESENT.
   */
  public static final String AUTHORIZATION_HEADER_NOT_PRESENT = "authorization.header.not.present";

  /**
   * Private NoArgs Constructor.
   */
  private ExceptionConstants() {
    // EMPTY
  }
}
