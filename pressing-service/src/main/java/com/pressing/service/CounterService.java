package com.pressing.service;

import com.pressing.model.Counter;
import com.pressing.model.Merchant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CounterService {

  Counter save(Counter counter);

  Counter findById(Long id);

  Page<Counter> findAll(Pageable pageable);

  void delete(Long id);

  int countByAgency_Merchant(Merchant merchant);
}
