package com.pressing.service;

import com.pressing.model.Agency;
import com.pressing.model.Merchant;
import java.util.List;

public interface AgencyService {

  Agency save(Agency agency);

  Agency findById(Long id);

  List<Agency> findAll();

  void delete(Long id);

  int countByMerchant(Merchant merchant);
}
