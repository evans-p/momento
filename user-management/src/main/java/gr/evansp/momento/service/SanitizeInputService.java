package gr.evansp.momento.service;

import gr.evansp.momento.model.UserProfile;
import org.springframework.stereotype.Service;

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
