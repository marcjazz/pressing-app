package com.pressing.repository;

import com.pressing.model.Counter;
import com.pressing.model.Customer;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Customer findByEmail(@Param("email") String email);

  Collection<Customer> findByIsActive(@Param("isActive") boolean isActive);

  Page<Customer> findByCounter(Counter counter, Pageable pageable);

  Page<Customer> findByIsActiveAndCounter(boolean isActive, Counter counter, Pageable pageable);
}
