package org.example.mtsroutingproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * DTO for {@link org.example.mtsroutingproject.model.DataRecord}
 */
@Data
@Validated
public class DataRecordDto implements Serializable {
  @NotNull
  @Min(0)
  private final Integer type;

  @NotBlank
  @Size(max = 1000)
  private final String text;

  /**
   * DTO for DataRecord
   * @param type special int
   * @param text special text
   */
  public DataRecordDto(Integer type, String text) {
    this.type = type;
    this.text = text;
  }
}
