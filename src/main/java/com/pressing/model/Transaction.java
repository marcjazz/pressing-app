package com.pressing.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {

  @Id @GeneratedValue private Long id;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @ManyToOne(optional = false)
  @JoinColumn(name = "customer_id", referencedColumnName = "id")
  private Customer customer;

  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @ManyToOne(optional = false)
  @JoinColumn(name = "item_id", referencedColumnName = "id")
  private Item item;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private String status;

  @Column(nullable = false, unique = true)
  private String label;

  @Column(nullable = false)
  private Date depositDate = new Date();

  @Column(nullable = false)
  private Date dueDate;

  @Column(nullable = false)
  @OneToMany(mappedBy = "customerItem", cascade = CascadeType.ALL)
  private List<Payment> payments;
}
