package org.example.mtsroutingproject.controller;

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
  public ResponseEntity<UUID> save(@RequestBody DataRecordDto dataRecordDto) {
    if (dataRecordDto.getType() < 0) {
      log.warn("Invalid type {}", dataRecordDto.getType());
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    DataRecord saved = new DataRecord();
    saved.setText(dataRecordDto.getText());
    saved.setType(dataRecordDto.getType());

    log.debug("Saving data record {}", saved);
    dataRecordService.save(saved);

    return ResponseEntity.ok(saved.getId());
  }
}
