package com.pressing.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
public class PaymentMethod {

  @NonNull @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = true)
  private String description;

  @Column(nullable = false)
  private boolean isActive;

  @ManyToOne
  @JoinColumn(name = "merchant_id")
  private Merchant merchant;

  @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
  private List<Payment> payments;
}
