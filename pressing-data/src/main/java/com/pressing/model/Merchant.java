package com.pressing.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "merchants")
public class Merchant extends AbstractEntity {

  private String name;

  private String description;

  private String email;

  private String telephone;

  private Instant registrationDate;

  private boolean active;

  @ManyToOne
  @JoinColumn(name = "plan_id")
  private Plan plan;
}
