package gr.evansp.momento.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Abstract Exception. To be extended by all application Exceptions.
 */
@Getter
@AllArgsConstructor
abstract class AbstractException extends RuntimeException {
  /**
   * Message code.
   */
  final String message;

  /**
   * Arguments for message.
   */
  final Object[] args;
}
