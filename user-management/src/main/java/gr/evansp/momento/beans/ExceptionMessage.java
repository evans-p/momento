package gr.evansp.momento.beans;

import java.util.Map;

/**
 * record to be returned when an {@link Exception} occurs.
 *
 * @param messages
 * 		messages
 */
public record ExceptionMessage(Map<String, String> messages) {}
