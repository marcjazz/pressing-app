package com.pressing.model;

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
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialPurchase {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private Date purchasedDate = new Date();

  @Column(nullable = false)
  private Date depreciationDate = new Date();

  @ManyToOne(optional = false)
  @JoinColumn(name = "cleaning_material_id", referencedColumnName = "id")
  private CleaningMaterial cleaningMaterial;
}
