package gr.evansp.momento.util;

import java.net.MalformedURLException;
import java.net.URI;

import org.owasp.encoder.Encode;

/**
 * Utility class for input sanitization.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class InputSanitizer {

	private InputSanitizer() {
		//EMPTY
	}

	public static String sanitizeStringInput(String input) {
		return Encode.forHtml(input);
	}

	public static String sanitizeUrl(String url) {
		String sanitizedUrl = null;
		if (url != null) {
			try {
				URI.create(url).toURL();
				sanitizedUrl = Encode.forUriComponent(url);
			} catch (MalformedURLException ignored) {
			}
		}

		return sanitizedUrl;
	}
}
