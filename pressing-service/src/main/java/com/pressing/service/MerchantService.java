package com.pressing.service;

import com.pressing.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MerchantService {

  Merchant save(Merchant merchant);

  Merchant findById(Long id);

  Page<Merchant> findAll(Pageable pageable);

  void delete(Long id);
}
