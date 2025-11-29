package com.pressing.repository;

import com.pressing.model.CleaningMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CleaningMaterialRepository extends JpaRepository<CleaningMaterial, Long> {

  CleaningMaterial findByName(@Param("name") String name);
}
