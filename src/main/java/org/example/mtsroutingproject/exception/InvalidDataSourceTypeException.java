package org.example.mtsroutingproject.exception;

public class InvalidDataSourceTypeException extends RuntimeException {
  /**
   * Exception throws if failed to peek database
   * @param type special number
   */
  public InvalidDataSourceTypeException(Integer type) {
    super("Invalid database type: " + type);
  }
}
