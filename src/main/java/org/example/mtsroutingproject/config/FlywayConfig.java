package org.example.mtsroutingproject.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

  @Value("${spring.flyway.locations}")
  private String location;

  /**
   * Migrations for first PostgreSQL database
   * @param dataSource instance for first database
   * @return result of migration
   */
  @Bean
  public MigrateResult firstDb(@Qualifier("primaryDataSource") DataSource dataSource) {
    return Flyway.configure()
        .dataSource(dataSource)
        .locations(location)
        .load()
        .migrate();
  }

  /**
   * Migrations for second PostgreSQL database
   * @param dataSource instance for second database
   * @return result of migration
   */
  @Bean
  public MigrateResult secondDb(@Qualifier("secondaryDataSource") DataSource dataSource) {
    return Flyway.configure()
        .dataSource(dataSource)
        .locations(location)
        .load()
        .migrate();
  }

  /**
   * Migrations for third PostgreSQL database
   * @param dataSource instance for third database
   * @return result of migration
   */
  @Bean
  public MigrateResult thirdDb(@Qualifier("tertiaryDataSource") DataSource dataSource) {
    return Flyway.configure()
        .dataSource(dataSource)
        .locations(location)
        .load()
        .migrate();
  }
}
