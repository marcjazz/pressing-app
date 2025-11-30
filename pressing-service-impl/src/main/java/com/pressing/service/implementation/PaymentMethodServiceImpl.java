package com.pressing.service.implementation;

import com.pressing.model.Merchant;
import com.pressing.model.PaymentMethod;
import com.pressing.repository.PaymentMethodRepository;
import com.pressing.service.PaymentMethodService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ROLE_ADMINISTRATION")
public class PaymentMethodServiceImpl implements PaymentMethodService {

  @Autowired private PaymentMethodRepository paymentMethodRepository;

  @Override
  @Cacheable("paymentMethods")
  public Page<PaymentMethod> findAll(Pageable pageable) {
    return paymentMethodRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "paymentMethod", key = "#id")
  public PaymentMethod findById(Long id) {
    if (id == null) {
      return null;
    }

    PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElse(null);
    return paymentMethod;
  }

  @Override
  public PaymentMethod findByName(String name) {

    PaymentMethod paymentMethod = paymentMethodRepository.findByName(name);
    return paymentMethod;
  }

  @Override
  public Collection<PaymentMethod> findByIsActive(boolean isActive) {
    return paymentMethodRepository.findByIsActive(isActive);
  }

  @Override
  @Cacheable(value = "paymentMethodsByMerchant", key = "#merchant.id")
  public Page<PaymentMethod> findByMerchant(Merchant merchant, Pageable pageable) {
    return paymentMethodRepository.findByMerchant(merchant, pageable);
  }

  @Override
  @Cacheable(value = "paymentMethodsByMerchantAndActive", key = "#merchant.id + '-' + #isActive")
  public Page<PaymentMethod> findByMerchantAndIsActive(
      Merchant merchant, boolean isActive, Pageable pageable) {
    return paymentMethodRepository.findByMerchantAndIsActive(merchant, isActive, pageable);
  }

  @Override
  @CacheEvict(
      value = {
        "paymentMethods",
        "paymentMethod",
        "paymentMethodsByMerchant",
        "paymentMethodsByMerchantAndActive"
      },
      allEntries = true)
  public PaymentMethod create(PaymentMethod paymentMethod) {
    if (paymentMethod == null) {
      return null;
    }

    if (paymentMethodRepository.existsById(paymentMethod.getId())) {
      return null;
    }

    PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
    return savedPaymentMethod;
  }

  @Override
  @CacheEvict(
      value = {
        "paymentMethods",
        "paymentMethod",
        "paymentMethodsByMerchant",
        "paymentMethodsByMerchantAndActive"
      },
      allEntries = true)
  public PaymentMethod update(PaymentMethod paymentMethod) {
    if (paymentMethod == null) {
      return null;
    }

    if (paymentMethodRepository.existsById(paymentMethod.getId())) {
      PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
      return savedPaymentMethod;
    }

    return null;
  }

  @Override
  @CacheEvict(
      value = {
        "paymentMethods",
        "paymentMethod",
        "paymentMethodsByMerchant",
        "paymentMethodsByMerchantAndActive"
      },
      allEntries = true)
  public void deactivate(Long id) {

    PaymentMethod paymentMethod = findById(id);
    paymentMethod.setActive(false);
  }
}
