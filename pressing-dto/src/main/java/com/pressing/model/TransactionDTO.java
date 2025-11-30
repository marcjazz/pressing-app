package com.pressing.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
  private Long id;
  private Long customerId;
  private Long itemId;
  private Integer quantity;
  private String status;
  private String label;
  private Date depositDate;
  private Date dueDate;
}