package com.pressing.service;

import com.pressing.model.Agency;
import com.pressing.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgencyService {

  Agency save(Agency agency);

  Agency findById(Long id);

  Page<Agency> findAll(Pageable pageable);

  void delete(Long id);

  int countByMerchant(Merchant merchant);
}
