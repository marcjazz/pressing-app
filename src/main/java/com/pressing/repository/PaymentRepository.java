package com.pressing.repository;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.pressing.model.Transaction;
import com.pressing.model.Payment;
import com.pressing.model.PaymentMethod;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	
	Collection<Payment> findByCustomerItem(@Param("customerItem") Transaction customerItem);
	Collection<Payment> findByPaymentMethod(@Param("paymentMethod") PaymentMethod paymentMethod);
	Collection<Payment> findByTime(@Param("time") Date time);

}
