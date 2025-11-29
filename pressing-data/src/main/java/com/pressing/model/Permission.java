package com.pressing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  @ManyToMany(mappedBy = "permissions")
  private List<Role> roles;
}
