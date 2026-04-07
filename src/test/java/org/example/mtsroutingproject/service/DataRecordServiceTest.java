package org.example.mtsroutingproject.service;

import org.example.mtsroutingproject.model.DataRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Testcontainers
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
public class DataRecordServiceTest {

  @Container
  static final PostgreSQLContainer<?> PRIMARY =
      new PostgreSQLContainer<>("postgres:16")
          .withDatabaseName("first_test")
          .withUsername("test")
          .withPassword("test");

  @Container
  static final PostgreSQLContainer<?> SECONDARY =
      new PostgreSQLContainer<>("postgres:16")
          .withDatabaseName("second_test")
          .withUsername("test")
          .withPassword("test");

  @Container
  static final PostgreSQLContainer<?> TERTIARY =
      new PostgreSQLContainer<>("postgres:16")
          .withDatabaseName("third_test")
          .withUsername("test")
          .withPassword("test");

  @DynamicPropertySource
  static void overrideProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.primary.url", PRIMARY::getJdbcUrl);
    registry.add("spring.datasource.primary.username", PRIMARY::getUsername);
    registry.add("spring.datasource.primary.password", PRIMARY::getPassword);

    registry.add("spring.datasource.secondary.url", SECONDARY::getJdbcUrl);
    registry.add("spring.datasource.secondary.username", SECONDARY::getUsername);
    registry.add("spring.datasource.secondary.password", SECONDARY::getPassword);

    registry.add("spring.datasource.tertiary.url", TERTIARY::getJdbcUrl);
    registry.add("spring.datasource.tertiary.username", TERTIARY::getUsername);
    registry.add("spring.datasource.tertiary.password", TERTIARY::getPassword);
  }

  @Autowired
  private DataRecordService dataRecordService;

  @Test
  void testPrimaryHealthy() {
    Assertions.assertTrue(PRIMARY.isRunning());
  }

  @Test
  void testSecondaryHealthy() {
    Assertions.assertTrue(SECONDARY.isRunning());
  }

  @Test
  void testTertiaryHealthy() {
    Assertions.assertTrue(TERTIARY.isRunning());
  }

  @Test
  void saveToPrimary() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(0);
    dataRecord.setText("test");
    DataRecord saved = dataRecordService.save(dataRecord);

    Assertions.assertNotNull(saved);
    Assertions.assertNotNull(saved.getId());
    Assertions.assertNotNull(saved.getCreatedAt());
  }

  @Test
  void saveToSecondary() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("test");
    DataRecord saved = dataRecordService.save(dataRecord);

    Assertions.assertNotNull(saved);
    Assertions.assertNotNull(saved.getId());
    Assertions.assertNotNull(saved.getCreatedAt());
  }

  @Test
  void saveToManyDb() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(2);
    dataRecord.setText("third text");
    dataRecordService.save(dataRecord);

    DataRecord dataRecord2 = new DataRecord();
    dataRecord2.setType(3);
    dataRecord2.setText("first text");
    dataRecordService.save(dataRecord2);

    List<DataRecord> found = dataRecordService.findByType(2);
    Assertions.assertNotNull(found);

    Assertions.assertEquals(1, found.size());
    Assertions.assertNotNull(found.get(0).getId());
    Assertions.assertEquals("third text", found.get(0).getText());
  }
}
