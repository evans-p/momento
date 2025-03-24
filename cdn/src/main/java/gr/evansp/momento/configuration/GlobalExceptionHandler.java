package gr.evansp.momento.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import gr.evansp.momento.bean.ConstraintViolationExceptionMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Application Global Exception Handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handler for invalid Entities.
	 *
	 * @param e {@link ConstraintViolationException}.
	 * @param locale locale.
	 * @return {@link ResponseEntity}.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ConstraintViolationExceptionMessage> handleInvalid(
			ConstraintViolationException e, Locale locale) {
		Map<String, String> messages =
				e.getConstraintViolations().stream()
						.collect(
								Collectors.toMap(
										c -> {
											List<String> paths =
													Arrays.asList(c.getPropertyPath().toString().split("\\."));
											return paths.getLast();
										},
										ConstraintViolation::getMessage));
		return new ResponseEntity<>(new ConstraintViolationExceptionMessage(messages), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
