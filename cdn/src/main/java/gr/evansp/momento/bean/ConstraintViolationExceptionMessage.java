package gr.evansp.momento.bean;

import java.util.Map;

import jakarta.validation.ConstraintViolationException;

/**
 * Bean record to be returned on the event of {@link ConstraintViolationException}.
 *
 * @param messages
 * 		messages to be returned.
 */
public record ConstraintViolationExceptionMessage(Map<String, String> messages) {}
