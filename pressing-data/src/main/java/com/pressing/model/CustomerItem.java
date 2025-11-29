package com.pressing.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerItem {

  private Long id = null;
  private Customer customer;
  private Item item;
  private int quantity;
  private String status;
  private String label;
  private Date depositDate = new Date();
  private Date dueDate;
}
