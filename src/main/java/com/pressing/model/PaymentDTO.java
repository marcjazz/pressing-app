package com.pressing.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

  private Long customerItemId;
  private Long paymentMethodId;
  private Date paymentDate;
  private Double amount;
}
