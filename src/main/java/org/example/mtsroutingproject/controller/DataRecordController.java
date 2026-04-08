package org.example.mtsroutingproject.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.mtsroutingproject.dto.DataRecordDto;
import org.example.mtsroutingproject.model.DataRecord;
import org.example.mtsroutingproject.service.DataRecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/data")
public class DataRecordController {
  private final DataRecordService dataRecordService;

  public DataRecordController(DataRecordService dataRecordService) {
    this.dataRecordService = dataRecordService;
  }

  @GetMapping
  public ResponseEntity<List<DataRecordDto>> getByType(@RequestParam("type") Integer type) {
    if (type == null || type < 0) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

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

  @PostMapping
  public ResponseEntity<UUID> save(@Valid @RequestBody DataRecordDto dataRecordDto) {
    DataRecord saved = new DataRecord();
    saved.setText(dataRecordDto.getText());
    saved.setType(dataRecordDto.getType());

    log.debug("Saving data record {}", saved);
    DataRecord result = dataRecordService.save(saved);
    return ResponseEntity.ok(result.getId());
  }
}
