package gr.evansp.momento.configuration;

import static gr.evansp.momento.constants.ExceptionConstants.*;

import gr.evansp.momento.bean.ExceptionMessage;
import gr.evansp.momento.exception.InternalServiceException;
import gr.evansp.momento.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Application Global Exception Handler.
 */
@SuppressWarnings("unused")
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /**
   * {@link MessageSource}.
   */
  MessageSource messageSource;

  /**
   * Constructor.
   *
   * @param messageSource
   *        {@link MessageSource}.
   */
  @Autowired
  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Handler for {@link NoResourceFoundException}.
   *
   * @param e
   *        {@link NoResourceFoundException}.
   * @param locale
   * 		locale
   * @return error message
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ExceptionMessage> handleNoResourceFoundException(
      NoResourceFoundException e, Locale locale) {
    String errorMessage = messageSource.getMessage(RESOURCE_NOT_FOUND, null, locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(RESOURCE_NOT_FOUND, errorMessage)), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for {@link MethodArgumentTypeMismatchException}.
   *
   * @param e
   *        {@link MethodArgumentTypeMismatchException}.
   * @param locale
   * 		locale
   * @return error message
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ExceptionMessage> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException e, Locale locale) {
    String errorMessage = messageSource.getMessage(CANNOT_PROCESS_REQUEST, null, locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(CANNOT_PROCESS_REQUEST, errorMessage)), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for {@link MethodArgumentTypeMismatchException}.
   *
   * @param e
   *        {@link MethodArgumentTypeMismatchException}.
   * @param locale
   * 		locale
   * @return error message
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ExceptionMessage> handleMaxUploadSizeExceeded(
      MaxUploadSizeExceededException e, Locale locale) {
    String errorMessage = messageSource.getMessage(PAYLOAD_TOO_LARGE, null, locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(PAYLOAD_TOO_LARGE, errorMessage)),
        HttpStatus.PAYLOAD_TOO_LARGE);
  }

  /**
   * Handler for {@link HttpMessageNotReadableException}.
   *
   * @param e
   *        {@link HttpMessageNotReadableException}.
   * @param locale
   * 		locale
   * @return error message
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ExceptionMessage> handleHttpMessageNotReadable(
      HttpMessageNotReadableException e, Locale locale) {
    String errorMessage = messageSource.getMessage(FAULTY_MESSAGE_BODY, null, locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(FAULTY_MESSAGE_BODY, errorMessage)), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for {@link HttpMediaTypeNotSupportedException}.
   *
   * @param e
   *        {@link HttpMediaTypeNotSupportedException}.
   * @param locale
   * 		locale
   * @return error message
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ExceptionMessage> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException e, Locale locale) {
    String errorMessage = messageSource.getMessage(MEDIA_TYPE_NOT_SUPPORTED, null, locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(MEDIA_TYPE_NOT_SUPPORTED, errorMessage)),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Handler for {@link HttpRequestMethodNotSupportedException}.
   *
   * @param e
   *        {@link HttpRequestMethodNotSupportedException}.
   * @param locale
   * 		locale
   * @return error message
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ExceptionMessage> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException e, Locale locale) {
    String errorMessage = messageSource.getMessage(METHOD_NOT_SUPPORTED, null, locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(METHOD_NOT_SUPPORTED, errorMessage)),
        HttpStatus.METHOD_NOT_ALLOWED);
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
  public ResponseEntity<ExceptionMessage> handleInternalServiceException(
      InternalServiceException e, Locale locale) {
    String errorMessage = messageSource.getMessage(e.getMessage(), e.getArgs(), locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(e.getMessage(), errorMessage)),
        HttpStatus.INTERNAL_SERVER_ERROR);
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
  public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(
      ResourceNotFoundException e, Locale locale) {
    String errorMessage = messageSource.getMessage(e.getMessage(), e.getArgs(), locale);
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(e.getMessage(), errorMessage)), HttpStatus.NOT_FOUND);
  }

  /**
   * Handler for invalid Entities.
   *
   * @param e
   *        {@link ConstraintViolationException}.
   * @return {@link ResponseEntity}.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ExceptionMessage> handleInvalid(ConstraintViolationException e) {
    Map<String, String> messages =
        e.getConstraintViolations().stream()
            .collect(
                Collectors.toMap(
                    c -> c.getMessageTemplate().replace("{", "").replace("}", ""),
                    ConstraintViolation::getMessage));
    return new ResponseEntity<>(new ExceptionMessage(messages), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  /**
   * If none of the exceptions above work, this one takes effect.
   *
   * @param e
   *        {@link Exception}.
   * @param locale
   * 		locale.
   * @return {@link ResponseEntity}
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionMessage> handleGenericException(Exception e, Locale locale) {
    String errorMessage = messageSource.getMessage(INTERNAL_SERVER_ERROR, null, locale);
    System.out.println(e.getClass());
    log.warn("Internal server error. Message: {}", e.getMessage());
    return new ResponseEntity<>(
        new ExceptionMessage(Map.of(INTERNAL_SERVER_ERROR, errorMessage)),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
