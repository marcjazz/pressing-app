package com.pressing.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.pressing.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

	PaymentMethod findByName(@Param("name") String name);
	Collection<PaymentMethod> findByIsActive(@Param("isActive") boolean isActive);
	
}
