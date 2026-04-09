package org.example.mtsroutingproject.exception;

public class DataRecordSaveException extends RuntimeException {
  /**
   * Throws if failed to save data in database
   */
  public DataRecordSaveException(String message) {
    super(message);
  }
}
