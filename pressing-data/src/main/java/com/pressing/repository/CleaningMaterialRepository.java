package com.pressing.repository;

import com.pressing.model.CleaningMaterial;
import com.pressing.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CleaningMaterialRepository extends JpaRepository<CleaningMaterial, Long> {

  CleaningMaterial findByName(@Param("name") String name);

  Page<CleaningMaterial> findByMerchant(Merchant merchant, Pageable pageable);
}
