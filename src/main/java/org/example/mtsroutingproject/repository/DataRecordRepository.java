package org.example.mtsroutingproject.repository;

import org.example.mtsroutingproject.model.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA repository for DataRecords
 */
@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, UUID> {
  /**
   * Find entities by type
   * @param type
   * @return list of found entities
   */
  List<DataRecord> findByType(Integer type);
}
