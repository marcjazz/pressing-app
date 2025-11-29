package com.pressing.repository;

import com.pressing.model.Counter;
import com.pressing.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CounterRepository extends JpaRepository<Counter, Long> {
  @Query("SELECT count(c) FROM Counter c WHERE c.agency.merchant = :merchant")
  int countByAgency_Merchant(@Param("merchant") Merchant merchant);
}
