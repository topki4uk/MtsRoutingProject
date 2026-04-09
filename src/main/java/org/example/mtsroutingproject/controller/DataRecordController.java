package org.example.mtsroutingproject.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.mtsroutingproject.dto.DataRecordDto;
import org.example.mtsroutingproject.model.DataRecord;
import org.example.mtsroutingproject.service.DataRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.UUID;

/**
 * App REST controller which contains 2 methods: GET and POST.
 * Get list of data records by its type (GET)
 * Save data record to routed database (POST)
 */
@Slf4j
@RestController
@RequestMapping("/api/data")
public class DataRecordController {
  private final DataRecordService dataRecordService;

  /**
   * DataRecordController
   * @param dataRecordService record service
   */
  public DataRecordController(DataRecordService dataRecordService) {
    this.dataRecordService = dataRecordService;
  }

  /**
   * GET method which found records by its type
   * @param type special number
   * @return list of DTOs found and response status
   */
  @GetMapping
  public ResponseEntity<List<DataRecordDto>> getByType(@RequestParam("type") Integer type) {
    List<DataRecordDto> records = dataRecordService
        .findByType(type)
        .stream()
        .map(DataRecord::toDto)
        .toList();

    if (records.isEmpty()) {
      log.warn("No records found for type {}", type);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    log.debug("Found {} records", records.size());
    return new ResponseEntity<>(records, HttpStatus.OK);
  }

  /**
   * Save data record in database
   * @param dataRecordDto data instance
   * @return UUID and response status
   */
  @PostMapping
  public ResponseEntity<UUID> save(@Valid @RequestBody DataRecordDto dataRecordDto) {
    DataRecord saved = DataRecord.fromDto(dataRecordDto);

    log.debug("Saving data record {}", saved);
    DataRecord result = dataRecordService.save(saved);
    return new ResponseEntity<>(result.getId(), HttpStatus.CREATED);
  }
}
