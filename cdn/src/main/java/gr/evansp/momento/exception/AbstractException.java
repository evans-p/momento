package gr.evansp.momento.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Abstract Exception. To be extended by all application Exceptions.
 */
@Getter
@AllArgsConstructor
abstract class AbstractException extends Exception {

	final String message;
	final Object[] args;
}
