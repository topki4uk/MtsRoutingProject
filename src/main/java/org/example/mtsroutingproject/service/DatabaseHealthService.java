package org.example.mtsroutingproject.service;

import lombok.extern.slf4j.Slf4j;
import org.example.mtsroutingproject.routing.DataSourceKey;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DatabaseHealthService {

  private final Map<DataSourceKey, DataSource> dataSources;

  /**
   * Constructor for database validator
   * @param dataSources
   */
  public DatabaseHealthService(
      @Qualifier("routingTargets") Map<DataSourceKey, DataSource> dataSources
  ) {
    this.dataSources = dataSources;
  }

  /**
   * Returns list of currently available DataSourceKey-s.
   */
  public List<DataSourceKey> getAvailableSources() {
    return dataSources.entrySet().stream()
        .filter(e -> isAlive(e.getKey(), e.getValue()))
        .map(Map.Entry::getKey)
        .toList();
  }

  /**
   * Checks for database alive
   * @param key current database
   * @param ds source for database
   * @return
   */
  public boolean isAlive(DataSourceKey key, DataSource ds) {
    try (Connection conn = ds.getConnection()) {
      return conn.isValid(2); // таймаут 2 секунды
    } catch (Exception e) {
      log.warn("DataSource {} is unavailable: {}", key, e.getMessage());
      return false;
    }
  }
}
