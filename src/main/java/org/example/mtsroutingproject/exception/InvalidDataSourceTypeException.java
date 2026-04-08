package org.example.mtsroutingproject.exception;

public class InvalidDataSourceTypeException extends RuntimeException {
  public InvalidDataSourceTypeException(Integer type) {
    super("Invalid database type: " + type);
  }
}
