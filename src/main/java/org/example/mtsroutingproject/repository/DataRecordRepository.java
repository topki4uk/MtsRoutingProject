package org.example.mtsroutingproject.repository;

import org.example.mtsroutingproject.model.DataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DataRecordRepository extends JpaRepository<DataRecord, UUID> {
  List<DataRecord> findByType(Integer type);
}