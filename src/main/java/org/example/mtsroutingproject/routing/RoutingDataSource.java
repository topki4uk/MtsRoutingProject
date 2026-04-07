package org.example.mtsroutingproject.routing;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
  @Override
  protected @Nullable Object determineCurrentLookupKey() {
    return DataSourceContext.getContext();
  }
}
