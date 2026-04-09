package org.example.mtsroutingproject.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.mtsroutingproject.dto.DataRecordDto;
import org.example.mtsroutingproject.exception.GlobalExceptionHandler;
import org.example.mtsroutingproject.model.DataRecord;
import org.example.mtsroutingproject.service.DataRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class DataRecordControllerTest {

  @Mock private DataRecordService dataRecordService;
  ObjectMapper objectMapper = new ObjectMapper();
  MockMvc mvc;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders
        .standaloneSetup(new DataRecordController(dataRecordService))
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  void getByTypeReturns200() throws Exception {
    DataRecord dataRecord = new DataRecord();
    dataRecord.setType(1);
    dataRecord.setText("text");
    when(dataRecordService.findByType(1)).thenReturn(List.of(dataRecord));

    String result = "[{\"type\":1,\"text\":\"text\"}]";
    mvc.perform(get("/api/data").param("type", "1"))
        .andExpect(status().isOk())
        .andExpect(content().string(result));
  }

  @Test
  void getByTypeReturns404() throws Exception {
    when(dataRecordService.findByType(any())).thenReturn(List.of());
    mvc.perform(get("/api/data").param("type", "1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getByTypeReturns400() throws Exception {
    mvc.perform(get("/api/data"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void saveReturns201() throws Exception {
    DataRecordDto dto = new DataRecordDto(1, "text");

    DataRecord saved = DataRecord.fromDto(dto);
    saved.setId(UUID.randomUUID());
    when(dataRecordService.save(any())).thenReturn(saved);

    mvc.perform(post("/api/data")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
    .andExpect(content().string("\"" + saved.getId() + "\""));
  }

  @Test
  void saveReturns400() throws Exception {
    mvc.perform(post("/api/data")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void saveReturns400NegativeType() throws Exception {
    String body = "{\"type\": -1, \"text\": \"hello\"}";
    mvc.perform(post("/api/data")
        .contentType(MediaType.APPLICATION_JSON)
        .content(body)
    ).andExpect(status().isBadRequest());
  }
}
