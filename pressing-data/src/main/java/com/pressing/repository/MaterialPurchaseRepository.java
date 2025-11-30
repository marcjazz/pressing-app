package com.pressing.repository;

import com.pressing.model.Counter;
import com.pressing.model.MaterialPurchase;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface MaterialPurchaseRepository extends JpaRepository<MaterialPurchase, Long> {

  Collection<MaterialPurchase> findByPurchasedDate(@Param("date") Date date);

  Collection<MaterialPurchase> findByCleaningMaterialId(@Param("id") Long id);

  Page<MaterialPurchase> findByCounter(Counter counter, Pageable pageable);

  Page<MaterialPurchase> findByPurchasedDateAndCounter(
      Date date, Counter counter, Pageable pageable);
}
