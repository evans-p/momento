package gr.evansp.momento.service;

import org.springframework.stereotype.Service;
import gr.evansp.momento.model.UserProfile;

/**
 * {@link Service} for sanitizing user input.
 */
public interface SanitizeInputService {

	/**
	 * Sanitized string user input, like {@link UserProfile} fields.
	 * @param input input
	 * @return sanitized input.
	 */
	String sanitizeStringInput(String input);

	/**
	 * Sanitizes urls.
	 * @param url urls.
	 * @return sanitized url.
	 */
	String sanitizeUrl(String url);
}
