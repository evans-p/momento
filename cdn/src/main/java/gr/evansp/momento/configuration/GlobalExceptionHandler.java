package gr.evansp.momento.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import gr.evansp.momento.bean.ConstraintViolationExceptionMessage;
import gr.evansp.momento.bean.ExceptionMessage;
import gr.evansp.momento.exception.InternalServiceException;
import gr.evansp.momento.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Application Global Exception Handler.
 */
@ControllerAdvice
public class GlobalExceptionHandler {


	MessageSource messageSource;

	@Autowired
	public GlobalExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * Handler for {@link InternalServiceException}.
	 *
	 * @param e
	 *        {@link InternalServiceException}.
	 * @param locale
	 * 		locale
	 * @return error message
	 */
	@ExceptionHandler(InternalServiceException.class)
	public ResponseEntity<ExceptionMessage> handleInternalServiceException(InternalServiceException e, Locale locale) {
		String errorMessage = messageSource.getMessage(e.getMessage(), e.getArgs(), locale);
		return new ResponseEntity<>(new ExceptionMessage(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handler for {@link ResourceNotFoundException}.
	 *
	 * @param e
	 *        {@link ResourceNotFoundException}.
	 * @param locale
	 * 		locale
	 * @return error message
	 */
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(ResourceNotFoundException e, Locale locale) {
		String errorMessage = messageSource.getMessage(e.getMessage(), e.getArgs(), locale);
		return new ResponseEntity<>(new ExceptionMessage(errorMessage), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handler for invalid Entities.
	 *
	 * @param e
	 *        {@link ConstraintViolationException}.
	 * @return {@link ResponseEntity}.
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ConstraintViolationExceptionMessage> handleInvalid(ConstraintViolationException e) {
		Map<String, String> messages = e.getConstraintViolations().stream().collect(Collectors.toMap(c -> {
			List<String> paths = Arrays.asList(c.getPropertyPath().toString().split("\\."));
			return paths.getLast();
		}, ConstraintViolation::getMessage));
		return new ResponseEntity<>(new ConstraintViolationExceptionMessage(messages), HttpStatus.UNPROCESSABLE_ENTITY);
	}
}
