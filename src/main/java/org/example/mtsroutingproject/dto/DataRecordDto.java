package org.example.mtsroutingproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO for {@link org.example.mtsroutingproject.model.DataRecord}
 */
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

  public Integer getType() {
    return type;
  }

  public String getText() {
    return text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataRecordDto entity = (DataRecordDto) o;
    return Objects.equals(this.type, entity.type) &&
        Objects.equals(this.text, entity.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, text);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" +
        "type = " + type + ", " +
        "text = " + text + ")";
  }
}
