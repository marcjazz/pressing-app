package com.pressing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.pressing.model.CleaningMaterial;

public interface CleaningMaterialRepository extends JpaRepository<CleaningMaterial, Long> {

	CleaningMaterial findByName(@Param("name") String name);
	
}
