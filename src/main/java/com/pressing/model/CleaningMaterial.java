package com.pressing.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cleaning_material")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleaningMaterial {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  @Column(nullable = false)
  private Double cost;

  @OneToMany(mappedBy = "cleaningMaterial", cascade = CascadeType.ALL)
  private Collection<MaterialPurchase> materialPurchases;
}
