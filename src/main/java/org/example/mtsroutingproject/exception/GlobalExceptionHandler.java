package org.example.mtsroutingproject.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle errors from controller in DataRecordDto
   * @param e exception
   * @return BAD_REQUEST
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  /**
   * Handle error if failed to peek database
   * @param e exception
   * @return BAD_REQUEST
   */
  @ExceptionHandler(InvalidDataSourceTypeException.class)
  public ResponseEntity<String> handleInvalidDataSourceTypeException(InvalidDataSourceTypeException e) {
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
