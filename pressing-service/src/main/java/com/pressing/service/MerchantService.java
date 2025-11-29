package com.pressing.service;

import com.pressing.model.Merchant;
import java.util.List;

public interface MerchantService {

  Merchant save(Merchant merchant);

  Merchant findById(Long id);

  List<Merchant> findAll();

  void delete(Long id);
}
