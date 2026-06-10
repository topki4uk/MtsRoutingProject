package org.example.mtsroutingproject.service;

import org.example.mtsroutingproject.exception.DataRecordSaveException;
import org.example.mtsroutingproject.exception.DataSourceUnavailableException;
import org.example.mtsroutingproject.model.DataRecord;
import org.example.mtsroutingproject.repository.DataRecordRepository;
import org.example.mtsroutingproject.routing.DataSourceContext;
import org.example.mtsroutingproject.routing.DataSourceKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataRecordServiceUnitTest {

  @Mock
  private DataRecordRepository repository;

  @Mock
  private TransactionTemplate transactionTemplate;

  @Mock
  private DatabaseHealthService healthService;

  private DataRecordService service;

  @BeforeEach
  void setUp() {
    service = new DataRecordService(repository, transactionTemplate, healthService);
  }

  @AfterEach
  void tearDown() {
    DataSourceContext.clearContext();
  }

  @Test
  void findByTypeReturnsRecord() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");

    when(healthService.getAvailableSources()).thenReturn(List.of(
        DataSourceKey.PRIMARY,
        DataSourceKey.SECONDARY,
        DataSourceKey.TERTIARY
    ));
    when(repository.findByType(1)).thenReturn(List.of(dataRecord));

    List<DataRecord> result = service.findByType(1);

    Assertions.assertEquals(1, result.size());
    Assertions.assertEquals(dataRecord, result.get(0));
    Assertions.assertNull(DataSourceContext.getContext());
  }

  @Test
  void findByTypeClearsContextWhenRepositoryThrows() {
    when(healthService.getAvailableSources()).thenReturn(List.of(
        DataSourceKey.PRIMARY,
        DataSourceKey.SECONDARY,
        DataSourceKey.TERTIARY
    ));
    when(repository.findByType(1)).thenThrow(new RuntimeException("db error"));

    Assertions.assertThrows(RuntimeException.class, () -> service.findByType(1));
    Assertions.assertNull(DataSourceContext.getContext());
  }

  @Test
  void findByTypeThrowsWhenRequestedDataSourceUnavailable() {
    when(healthService.getAvailableSources()).thenReturn(List.of(
        DataSourceKey.PRIMARY,
        DataSourceKey.TERTIARY
    ));

    DataSourceUnavailableException ex =
        Assertions.assertThrows(DataSourceUnavailableException.class, () -> service.findByType(1));

    Assertions.assertEquals(DataSourceKey.SECONDARY, ex.getRequested());
    Assertions.assertEquals(List.of(DataSourceKey.PRIMARY, DataSourceKey.TERTIARY), ex.getAvailable());
    Assertions.assertNotNull(DataSourceContext.getContext());
    verifyNoInteractions(repository);
  }

  @Test
  void findByTypeReturnsEmptyList() {
    when(healthService.getAvailableSources()).thenReturn(List.of(
        DataSourceKey.PRIMARY,
        DataSourceKey.SECONDARY,
        DataSourceKey.TERTIARY
    ));
    when(repository.findByType(999)).thenReturn(List.of());

    List<DataRecord> found = service.findByType(999);

    Assertions.assertNotNull(found);
    Assertions.assertTrue(found.isEmpty());
    Assertions.assertNull(DataSourceContext.getContext());
  }

  @Test
  void saveReturnsRecord() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");

    when(healthService.getAvailableSources()).thenReturn(List.of(
        DataSourceKey.PRIMARY,
        DataSourceKey.SECONDARY,
        DataSourceKey.TERTIARY
    ));
    when(transactionTemplate.execute(any())).thenReturn(dataRecord);

    DataRecord result = service.save(dataRecord);

    Assertions.assertEquals(dataRecord, result);
    Assertions.assertNull(DataSourceContext.getContext());
  }

  @Test
  void saveThrowsExceptionWhenTransactionReturnsNull() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");

    when(healthService.getAvailableSources()).thenReturn(List.of(
        DataSourceKey.PRIMARY,
        DataSourceKey.SECONDARY,
        DataSourceKey.TERTIARY
    ));
    when(transactionTemplate.execute(any())).thenReturn(null);

    Assertions.assertThrows(DataRecordSaveException.class, () -> service.save(dataRecord));
    Assertions.assertNull(DataSourceContext.getContext());
  }

  @Test
  void saveThrowsWhenRequestedDataSourceUnavailable() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");

    when(healthService.getAvailableSources()).thenReturn(List.of(
        DataSourceKey.PRIMARY,
        DataSourceKey.TERTIARY
    ));

    DataSourceUnavailableException ex =
        Assertions.assertThrows(DataSourceUnavailableException.class, () -> service.save(dataRecord));

    Assertions.assertEquals(DataSourceKey.SECONDARY, ex.getRequested());
    Assertions.assertEquals(List.of(DataSourceKey.PRIMARY, DataSourceKey.TERTIARY), ex.getAvailable());
    Assertions.assertNotNull(DataSourceContext.getContext());
    verifyNoInteractions(repository);
  }
}
