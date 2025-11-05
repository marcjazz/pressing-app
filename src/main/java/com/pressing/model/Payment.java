package com.pressing.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false)
  private double amount;

  @Column(nullable = false)
  private Date time = new Date();

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @ManyToOne(optional = false)
  @JoinColumn(name = "customer_item_id", referencedColumnName = "id")
  private Transaction customerItem;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @ManyToOne(optional = false)
  @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
  private PaymentMethod paymentMethod;
}
