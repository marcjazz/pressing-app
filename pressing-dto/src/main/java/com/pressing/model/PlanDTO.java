package com.pressing.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {
  private Integer id;
  private String name;
  private String description;
  private Double price;
  private Integer maxAgencies;
  private Integer maxCounters;
  private Integer maxUsers;
}