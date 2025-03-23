package gr.evansp.momento.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
abstract class AbstractException extends Exception {

	final String message;
	final Object[] args;
}
