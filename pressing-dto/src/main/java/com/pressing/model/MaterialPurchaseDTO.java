package com.pressing.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialPurchaseDTO {
  private Long id;
  private Long cleaningMaterialId;
  private Double quantity;
  private Double cost;
  private Date purchaseDate;
}