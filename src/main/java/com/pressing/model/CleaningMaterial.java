package com.pressing.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;

@Entity
@Table(name = "cleaning_material")
public class CleaningMaterial {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  @Column(nullable = false)
  private Double cost;

  @OneToMany(mappedBy = "cleaningMaterial", cascade = CascadeType.ALL)
  private Collection<MaterialPurchase> materialPurchases;

  public CleaningMaterial() {
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

  public Double getCost() {
    return this.cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }
}
