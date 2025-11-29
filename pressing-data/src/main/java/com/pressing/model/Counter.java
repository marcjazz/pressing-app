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
@Table(name = "counters")
public class Counter extends AbstractEntity {

  private String name;

  private String description;

  private Instant registrationDate;

  private boolean active;

  @ManyToOne
  @JoinColumn(name = "agency_id")
  private Agency agency;
}
