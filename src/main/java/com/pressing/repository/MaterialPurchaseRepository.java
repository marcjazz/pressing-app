package com.pressing.repository;

import com.pressing.model.MaterialPurchase;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MaterialPurchaseRepository extends JpaRepository<MaterialPurchase, Long> {

  Collection<MaterialPurchase> findByPurchasedDate(@Param("date") Date date);

  Collection<MaterialPurchase> findByCleaningMaterialId(@Param("id") Long id);
}
