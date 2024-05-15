package br.dubidubibank.exceptions.handlers;

import br.dubidubibank.exceptions.ResourceNotFoundException;
import br.dubidubibank.records.ErrorRecord;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<ErrorRecord> handleAccessDenied(
      AccessDeniedException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.FORBIDDEN.value();
    ErrorRecord error =
        new ErrorRecord(
            "Access denied.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorRecord> handleConstraintViolation(
      ConstraintViolationException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorRecord error =
        new ErrorRecord(
            "Constraint violation.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorRecord> handleDataIntegrityViolation(
      DataIntegrityViolationException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorRecord error =
        new ErrorRecord(
            "Data integrity violation.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorRecord> handleEmptyResultDataAccess(
      EmptyResultDataAccessException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.NOT_FOUND.value();
    ErrorRecord error =
        new ErrorRecord(
            "Empty result data access.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorRecord> handleEntityNotFound(
      EntityNotFoundException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.NOT_FOUND.value();
    ErrorRecord error =
        new ErrorRecord(
            "Entity not found.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorRecord> handleIllegalArgument(
      IllegalArgumentException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorRecord error =
        new ErrorRecord(
            "Illegal argument.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorRecord> handleMethodArgumentNotValid(
      MethodArgumentNotValidException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorRecord error =
        new ErrorRecord(
            "Method argument not valid.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorRecord> handleResourceNotFound(
      ResourceNotFoundException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.NOT_FOUND.value();
    ErrorRecord error =
        new ErrorRecord(
            "Resource not found.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }

  @ExceptionHandler(TransactionSystemException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorRecord> handleTransactionSystem(
      TransactionSystemException exception, HttpServletRequest request) {
    int statusCode = HttpStatus.BAD_REQUEST.value();
    ErrorRecord error =
        new ErrorRecord(
            "Transaction system.",
            Instant.now(),
            exception.getMessage(),
            request.getRequestURI(),
            statusCode);
    return ResponseEntity.status(statusCode).body(error);
  }
}
