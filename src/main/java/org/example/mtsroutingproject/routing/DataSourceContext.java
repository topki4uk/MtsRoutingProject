package org.example.mtsroutingproject.routing;

public class DataSourceContext {
  private static final ThreadLocal<DataSourceKey> CONTEXT = new ThreadLocal<>();

  public static DataSourceKey getContext() {
    return CONTEXT.get();
  }

  /**
   * Set database for current operations
   * All operations will reference to chosen database
   * @param key database type
   */
  public static void setContext(DataSourceKey key) {
    DataSourceContext.CONTEXT.set(key);
  }

  /**
   * Drop context, all operations will reference to default database
   */
  public static void clearContext() {
    CONTEXT.remove();
  }
}
