/**
 * Copyright (c) 2015-2026 Michael Kuß
 */
package de.miq.dirama.exception;

import de.miq.dirama.dto.error.ApiErrorResponse;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "de.miq.dirama.controller.api")
public class RestExceptionControllerAdvice {

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ApiErrorResponse handleNotFoundException(NotFoundException ex) {
    return getApiErrorResponse(ex);
  }

  // Spring Security method security (e.g., @PreAuthorize) is handled on the level of Spring
  // interceptors, which means exception will be handled by ControllerAdvice
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ApiErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ApiErrorResponse handleAuthenticationException(AuthenticationException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiErrorResponse unhandledException(Exception ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrorResponse handleRuntimeException(RuntimeException ex) {
    return getApiErrorResponse(ex);
  }

  @ExceptionHandler(ExistingException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ApiErrorResponse handleExistingException(ExistingException ex) {
    return getApiErrorResponse(ex);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return getApiErrorResponse(new ValidationException(), errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ApiErrorResponse handleValidationExceptions(ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations()
        .forEach(
            (error) -> {
              String fieldName = error.getPropertyPath().toString();
              String errorMessage = error.getMessage();
              errors.put(fieldName, errorMessage);
            });
    return getApiErrorResponse(new ValidationException(), errors);
  }

  private ApiErrorResponse getApiErrorResponse(Exception ex) {
    return getApiErrorResponse(ex, null);
  }

  private ApiErrorResponse getApiErrorResponse(Exception ex, Map<String, String> details) {

    log.error("Exception occurred", ex);

    return new ApiErrorResponse(ex.getMessage(), details);
  }
}
