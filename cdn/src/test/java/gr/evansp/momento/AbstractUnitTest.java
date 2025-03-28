package gr.evansp.momento;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Base Class for Unit tests.
 */
public class AbstractUnitTest {
	/**
	 * Error Messages.
	 */
	protected static final ResourceBundle ERROR_MESSAGES =
			ResourceBundle.getBundle("messages", Locale.getDefault());

	/**
	 * Validation Messages.
	 */
	protected static final ResourceBundle VALIDATION_MESSAGES =
			ResourceBundle.getBundle("ValidationMessages", Locale.getDefault());
}
