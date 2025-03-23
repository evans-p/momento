package gr.evansp.momento.exception;

public class RuleException extends AbstractException {

	public static final String FILE_EMPTY = "file.empty";

	public RuleException(String message, Object[] args) {
		super(message, args);
	}
}
