package com.pressing.repository;

import com.pressing.model.Merchant;
import com.pressing.model.PaymentMethod;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

  PaymentMethod findByName(@Param("name") String name);

  Collection<PaymentMethod> findByIsActive(@Param("isActive") boolean isActive);

  Page<PaymentMethod> findByMerchant(Merchant merchant, Pageable pageable);

  Page<PaymentMethod> findByMerchantAndIsActive(Merchant merchant, boolean isActive, Pageable pageable);
}
