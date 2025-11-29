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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = true)
  private String description;

  @Column(nullable = false)
  private Double cost;

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  private Category category;

  @ManyToOne
  @JoinColumn(name = "merchant_id")
  private Merchant merchant;

  @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
  private List<Transaction> customerItems;
}
