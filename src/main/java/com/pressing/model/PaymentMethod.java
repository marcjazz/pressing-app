package com.pressing.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = true)
  private String description;

  @Column(nullable = false)
  private boolean isActive;

  @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL)
  private List<Payment> payments;

  public PaymentMethod() {
    super();
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActive() {
    return this.isActive;
  }

  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }
}
