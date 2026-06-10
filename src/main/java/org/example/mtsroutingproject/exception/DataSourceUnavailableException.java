package org.example.mtsroutingproject.exception;

import lombok.Getter;
import org.example.mtsroutingproject.routing.DataSourceKey;
import java.util.List;

@Getter
public class DataSourceUnavailableException extends RuntimeException {

  private final DataSourceKey requested;
  private final List<DataSourceKey> available;

  /**
   * Exception for database
   */
  public DataSourceUnavailableException(DataSourceKey requested, List<DataSourceKey> available) {
    super("DataSource " + requested + " is unavailable. Available: " + available);
    this.requested = requested;
    this.available = available;
  }

}
