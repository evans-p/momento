package gr.evansp.momento.exception;

/**
 * Exception regarding invalid logic.
 */
public class LogicException extends AbstractException {
  public LogicException(String message, Object[] args) {
    super(message, args);
  }
}
