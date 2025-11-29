package com.pressing.service;

import com.pressing.model.Merchant;
import com.pressing.model.PaymentMethod;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service that provides CRUD operations for payment methods */
public interface PaymentMethodService {

  /**
   * Get all payment methods in the system.
   *
   * @return Collection of payment methods
   */
  public Page<PaymentMethod> findAll(Pageable pageable);

  /**
   * Find a payment method by Id.
   *
   * @param id
   * @return PaymentMethod object if found, else return null
   */
  public PaymentMethod findById(Long id);

  /**
   * Find a payment method by name.
   *
   * @param name
   * @return Payment method object if found, else return null
   */
  public PaymentMethod findByName(String name);

  /**
   * Find active / deactivated payment methods.
   *
   * @param isActive
   * @return Collection PaymentMethod object
   */
  public Collection<PaymentMethod> findByIsActive(boolean isActive);

  public Page<PaymentMethod> findByMerchant(Merchant merchant, Pageable pageable);

  public Page<PaymentMethod> findByMerchantAndIsActive(Merchant merchant, boolean isActive, Pageable pageable);

  /**
   * Create new payment method.
   *
   * @param paymentMethod
   * @return PaymentMethod object (Created PaymentMethod object)
   */
  public PaymentMethod create(PaymentMethod paymentMethod);

  /**
   * Update an existing payment method's information.
   *
   * @param paymentMethod
   * @return PaymentMethod object (Updated PaymentMethod object)
   */
  public PaymentMethod update(PaymentMethod paymentMethod);

  /**
   * Deactivate a payment method .
   *
   * @param id
   */
  public void deactivate(Long id);
}
