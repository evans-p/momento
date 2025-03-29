package gr.evansp.momento;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;


import org.springframework.core.io.ClassPathResource;

/**
 * Base Class for Unit tests.
 */
public abstract class AbstractUnitTest {
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

	protected static final byte[] PNG_IMAGE = loadImage("images/sample.png");
	protected static final byte[] JPG_IMAGE = loadImage("images/sample.jpeg");

	private static byte[] loadImage(String imagePath) {
		ClassPathResource resource = new ClassPathResource(imagePath);

		try {
			return resource.getContentAsByteArray();
		} catch (IOException e) {
			return null;
		}
	}
}
