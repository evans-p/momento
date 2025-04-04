package gr.evansp.momento.exception;

/**
 * {@link Exception} to be thrown on an internal server error.
 */
public class InternalServiceException extends AbstractException {

	public static final String FILE_PROCESS_FAILED = "file.process.failed";

	/**
	 * All args constructor.
	 *
	 * @param message
	 * 		exception Code.
	 * @param args
	 * 		params.
	 */
	public InternalServiceException(String message, Object[] args) {
		super(message, args);
	}
}
