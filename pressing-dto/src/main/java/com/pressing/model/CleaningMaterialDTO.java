package com.pressing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleaningMaterialDTO {
  private Long id;
  private String name;
  private String description;
  private Double cost;
  private Double quantity;
  private String unit;
}
