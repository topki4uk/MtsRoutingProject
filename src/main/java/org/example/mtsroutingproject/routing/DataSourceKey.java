package org.example.mtsroutingproject.routing;

import lombok.Getter;

@Getter
public enum DataSourceKey {
  PRIMARY(0),
  SECONDARY(1),
  TERTIARY(2);

  private final int key;
  DataSourceKey(int key) {
    this.key = key;
  }

}
