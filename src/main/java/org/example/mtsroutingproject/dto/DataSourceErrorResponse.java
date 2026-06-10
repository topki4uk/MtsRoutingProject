package org.example.mtsroutingproject.dto;

import org.example.mtsroutingproject.routing.DataSourceKey;
import java.util.List;

public record DataSourceErrorResponse(
    String message,
    String requestedSource,
    List<String> availableSources
) {
  /**
   * Error response if connection with database failed
   * @param requested requested database
   * @param available list of available databases
   */
  public static DataSourceErrorResponse of(DataSourceKey requested, List<DataSourceKey> available) {
    return new DataSourceErrorResponse(
        "This database is currently unavailable",
        requested.name(),
        available.stream().map(Enum::name).toList()
    );
  }
}
