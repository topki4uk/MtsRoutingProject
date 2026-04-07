package org.example.mtsroutingproject.routing;

public class DataSourceContext {
  private static final ThreadLocal<DataSourceKey> context = new ThreadLocal<>();

  public static DataSourceKey getContext() {
    return context.get();
  }

  public static void setContext(DataSourceKey key) {
    DataSourceContext.context.set(key);
  }

  public static void clearContext() {
    context.remove();
  }
}
