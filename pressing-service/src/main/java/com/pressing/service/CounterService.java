package com.pressing.service;

import com.pressing.model.Counter;
import com.pressing.model.Merchant;
import java.util.List;

public interface CounterService {

  Counter save(Counter counter);

  Counter findById(Long id);

  List<Counter> findAll();

  void delete(Long id);

  int countByAgency_Merchant(Merchant merchant);
}
