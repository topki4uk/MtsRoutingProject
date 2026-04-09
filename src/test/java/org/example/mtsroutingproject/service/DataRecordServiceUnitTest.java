package org.example.mtsroutingproject.service;

import org.example.mtsroutingproject.exception.DataRecordSaveException;
import org.example.mtsroutingproject.exception.InvalidDataSourceTypeException;
import org.example.mtsroutingproject.model.DataRecord;
import org.example.mtsroutingproject.repository.DataRecordRepository;
import org.example.mtsroutingproject.routing.DataSourceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DataRecordServiceUnitTest {

  @Mock DataRecordRepository repository;
  @Mock TransactionTemplate transactionTemplate;
  @InjectMocks DataRecordService service;

  @Test
  void findByTypeReturnsRecord() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");

    when(repository.findByType(1)).thenReturn(List.of(dataRecord));
    List<DataRecord> result = service.findByType(1);

    Assertions.assertEquals(dataRecord, result.get(0));
  }

  @Test
  void findByTypeContext() {
    when(repository.findByType(any(Integer.class)))
        .thenThrow(new InvalidDataSourceTypeException(any(Integer.class)));

    Assertions.assertThrows(InvalidDataSourceTypeException.class, () -> service.findByType(0));
    Assertions.assertNull(DataSourceContext.getContext());
  }

  @Test
  void saveReturnsRecord() {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");

    when(transactionTemplate.execute(any())).thenReturn(dataRecord);
    DataRecord result = service.save(dataRecord);

    Assertions.assertEquals(dataRecord, result);
  }

  @Test
  void saveThrowsException() {
    when(transactionTemplate.execute(any())).thenReturn(null);

    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");

    Assertions.assertThrows(DataRecordSaveException.class, () -> service.save(dataRecord));
  }
}
