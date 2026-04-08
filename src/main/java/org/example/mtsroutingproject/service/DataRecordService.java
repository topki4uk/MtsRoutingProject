package org.example.mtsroutingproject.service;

import lombok.extern.slf4j.Slf4j;
import org.example.mtsroutingproject.exception.InvalidDataSourceTypeException;
import org.example.mtsroutingproject.model.DataRecord;
import org.example.mtsroutingproject.repository.DataRecordRepository;
import org.example.mtsroutingproject.routing.DataSourceContext;
import org.example.mtsroutingproject.routing.DataSourceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
public class DataRecordService {

  private final TransactionTemplate transactionTemplate;
  @Value("${spring.application.database-count:3}")
  private Integer databaseCount;

  private final DataRecordRepository dataRecordRepository;

  private DataSourceKey peekDatabaseByType(Integer type) {
    int preparedKey = type % databaseCount;

    for (DataSourceKey key : DataSourceKey.values()) {
      if (preparedKey == key.getKey()) {
        return key;
      }
    }

    throw new InvalidDataSourceTypeException(type);
  }

  public DataRecordService(DataRecordRepository dataRecordRepository, TransactionTemplate transactionTemplate) {
    this.dataRecordRepository = dataRecordRepository;
    this.transactionTemplate = transactionTemplate;
  }

  public List<DataRecord> findByType(Integer type) {
    DataSourceKey key = peekDatabaseByType(type);
    DataSourceContext.setContext(key);

    try {
      log.debug("Looking up data records for database {}", key);
      return dataRecordRepository.findByType(type);
    } catch(DataAccessException e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      DataSourceContext.clearContext();
    }
  }

  public DataRecord save(DataRecord dataRecord) {
    DataSourceKey key = peekDatabaseByType(dataRecord.getType());
    DataSourceContext.setContext(key);

    try {
      log.debug("Saving data record {}", dataRecord);
      DataRecord result = transactionTemplate.execute(status -> dataRecordRepository.save(dataRecord));
      if (result == null) {
        throw new RuntimeException("Could not save data record " + dataRecord);
      }
      return result;
    } catch(DataAccessException e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      DataSourceContext.clearContext();
    }
  }
}
