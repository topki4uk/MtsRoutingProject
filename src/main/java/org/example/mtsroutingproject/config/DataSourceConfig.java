package org.example.mtsroutingproject.config;

import org.example.mtsroutingproject.routing.DataSourceKey;
import org.example.mtsroutingproject.routing.RoutingDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

  /**
   * Special method that allows to load properties for database
   * @return DataSourceProperties
   */
  @Bean
  @ConfigurationProperties("spring.datasource.primary")
  public DataSourceProperties primaryProperties() {
    return new DataSourceProperties();
  }

  /**
   * Initialization data source
   * @return DataSource
   */
  @Bean
  public DataSource primaryDataSource() {
    return primaryProperties().initializeDataSourceBuilder().build();
  }

  /**
   * Special method that allows to load properties for database
   * @return DataSourceProperties
   */
  @Bean
  @ConfigurationProperties("spring.datasource.secondary")
  public DataSourceProperties secondaryProperties() {
    return new DataSourceProperties();
  }

  /**
   * Initialization data source
   * @return DataSource
   */
  @Bean
  public DataSource secondaryDataSource() {
    return secondaryProperties().initializeDataSourceBuilder().build();
  }

  /**
   * Special method that allows to load properties for database
   * @return DataSourceProperties
   */
  @Bean
  @ConfigurationProperties("spring.datasource.tertiary")
  public DataSourceProperties tertiaryProperties() {
    return new DataSourceProperties();
  }

  /**
   * Initialization data source
   * @return DataSource
   */
  @Bean
  public DataSource tertiaryDataSource() {
    return tertiaryProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  @Qualifier("routingTargets")
  public Map<DataSourceKey, DataSource> dataSources(
      @Qualifier("primaryDataSource") DataSource primary,
      @Qualifier("secondaryDataSource") DataSource secondary,
      @Qualifier("tertiaryDataSource") DataSource tertiary
  ) {
    return Map.of(
        DataSourceKey.PRIMARY, primary,
        DataSourceKey.SECONDARY, secondary,
        DataSourceKey.TERTIARY, tertiary
    );
  }

  /**
   * Setup targets for switching contexts in app
   * @return routing
   */
  @Primary
  @Bean
  public DataSource routingDataSource(@Qualifier("routingTargets") Map<DataSourceKey, DataSource> dataSources) {
    RoutingDataSource routing = new RoutingDataSource();
    routing.setTargetDataSources(new HashMap<>(dataSources));
    routing.setDefaultTargetDataSource(dataSources.get(DataSourceKey.PRIMARY));
    return routing;
  }
}
