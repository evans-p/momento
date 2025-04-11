package gr.evansp.momento.bean;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;

/**
 * Bean record to be returned on the event of {@link ConstraintViolationException}.
 *
 * @param messages
 * 		messages to be returned.
 */
public record ConstraintViolationExceptionMessage(Map<String, String> messages) {}
