package org.example.mtsroutingproject.routing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataSourceKeyTest {
  @Test
  void fromTypeZero() {
    DataSourceKey key = DataSourceKey.fromType(0);
    Assertions.assertEquals(DataSourceKey.PRIMARY, key);
  }

  @Test
  void fromTypeOne() {
    DataSourceKey key = DataSourceKey.fromType(1);
    Assertions.assertEquals(DataSourceKey.SECONDARY, key);
  }

  @Test
  void fromTypeTwo() {
    DataSourceKey key = DataSourceKey.fromType(2);
    Assertions.assertEquals(DataSourceKey.TERTIARY, key);
  }

  @Test
  void fromTypeCycle() {
    DataSourceKey key = DataSourceKey.fromType(3);
    Assertions.assertEquals(DataSourceKey.PRIMARY, key);
  }

  @Test
  void fromTypeLarge() {
    DataSourceKey key = DataSourceKey.fromType(100);
    Assertions.assertEquals(DataSourceKey.SECONDARY, key);
  }
}
