package org.example.mtsroutingproject.service;

import lombok.extern.slf4j.Slf4j;
import org.example.mtsroutingproject.exception.DataRecordSaveException;
import org.example.mtsroutingproject.model.DataRecord;
import org.example.mtsroutingproject.repository.DataRecordRepository;
import org.example.mtsroutingproject.routing.DataSourceContext;
import org.example.mtsroutingproject.routing.DataSourceKey;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Slf4j
@Service
public class DataRecordService {

  private final TransactionTemplate transactionTemplate;
  private final DataRecordRepository dataRecordRepository;

  /**
   * Constructor of service
   * @param dataRecordRepository record repository
   * @param transactionTemplate transaction template
   */
  public DataRecordService(DataRecordRepository dataRecordRepository, TransactionTemplate transactionTemplate) {
    this.dataRecordRepository = dataRecordRepository;
    this.transactionTemplate = transactionTemplate;
  }

  /**
   * Find entities in database following type
   * @param type special int
   * @return list of entities
   */
  public List<DataRecord> findByType(Integer type) {
    DataSourceKey key = DataSourceKey.fromType(type);
    DataSourceContext.setContext(key);

    try {
      log.debug("Looking up data records for database {}", key);
      return dataRecordRepository.findByType(type);
    } catch (DataAccessException e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      DataSourceContext.clearContext();
    }
  }

  /**
   * Save data in database
   * @param dataRecord data
   * @return instance of saved data
   */
  public DataRecord save(DataRecord dataRecord) {
    DataSourceKey key = DataSourceKey.fromType(dataRecord.getType());
    DataSourceContext.setContext(key);

    try {
      log.debug("Saving data record {}", dataRecord);
      DataRecord result = transactionTemplate.execute(status -> dataRecordRepository.save(dataRecord));
      if (result == null) {
        throw new DataRecordSaveException("Could not save data record " + dataRecord);
      }
      return result;
    } catch (DataAccessException e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      DataSourceContext.clearContext();
    }
  }
}
