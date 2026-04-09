package org.example.mtsroutingproject.routing;

import lombok.Getter;
import org.example.mtsroutingproject.exception.InvalidDataSourceTypeException;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum DataSourceKey {
  PRIMARY(0),
  SECONDARY(1),
  TERTIARY(2);

  private final int key;

  DataSourceKey(int key) {
    this.key = key;
  }

  private static final Map<Integer, DataSourceKey> KEY_MAP = Arrays.stream(values()).collect(
      Collectors.toUnmodifiableMap(DataSourceKey::getKey, Function.identity())
  );

  /**
   * Optimized method for getting DataSourceKey by type
   * @param key special integer
   * @return peeked data source
   */
  public static DataSourceKey fromType(int key) {
    int preparedKey = key % values().length;
    DataSourceKey result = KEY_MAP.get(preparedKey);

    if (result == null) {
      throw new InvalidDataSourceTypeException(key);
    }

    return result;
  }
}
