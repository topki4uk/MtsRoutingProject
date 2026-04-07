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

  @Bean
  @ConfigurationProperties("spring.datasource.primary")
  public DataSourceProperties primaryProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource primaryDataSource() {
    return primaryProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  @ConfigurationProperties("spring.datasource.secondary")
  public DataSourceProperties secondaryProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource secondaryDataSource() {
    return secondaryProperties().initializeDataSourceBuilder().build();
  }

  @Bean
  @ConfigurationProperties("spring.datasource.tertiary")
  public DataSourceProperties tertiaryProperties() {
    return new DataSourceProperties();
  }

  @Bean
  public DataSource tertiaryDataSource() {
    return tertiaryProperties().initializeDataSourceBuilder().build();
  }

  @Primary
  @Bean
  public DataSource routingDataSource(
      @Qualifier("primaryDataSource") DataSource primary,
      @Qualifier("secondaryDataSource") DataSource secondary,
      @Qualifier("tertiaryDataSource") DataSource tertiary) {
    Map<Object, Object> targets = new HashMap<>();
    targets.put(DataSourceKey.PRIMARY, primary);
    targets.put(DataSourceKey.SECONDARY, secondary);
    targets.put(DataSourceKey.TERTIARY, tertiary);

    RoutingDataSource routing = new RoutingDataSource();
    routing.setTargetDataSources(targets);
    routing.setDefaultTargetDataSource(primary);
    return routing;
  }
}
