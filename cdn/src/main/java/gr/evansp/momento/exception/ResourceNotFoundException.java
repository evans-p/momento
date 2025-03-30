package gr.evansp.momento.exception;

/**
 * {@link Exception} to be thrown when a resource is not found.
 */
public class ResourceNotFoundException extends AbstractException {

  /**
   * All args constructor.
   *
   * @param message exception Code.
   * @param args params.
   */
  public ResourceNotFoundException(String message, Object[] args) {
    super(message, args);
  }
}
