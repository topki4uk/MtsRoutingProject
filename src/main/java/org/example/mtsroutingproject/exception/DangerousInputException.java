package org.example.mtsroutingproject.exception;

public class DangerousInputException extends RuntimeException {
  /**
   * Throws if input contains SQL injection
   */
  public DangerousInputException(String message) {
    super(message);
  }
}
