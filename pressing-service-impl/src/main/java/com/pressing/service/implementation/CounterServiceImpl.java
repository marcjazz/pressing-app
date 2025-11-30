package com.pressing.service.implementation;

import com.pressing.model.Counter;
import com.pressing.repository.CounterRepository;
import com.pressing.service.CounterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CounterServiceImpl implements CounterService {

  private CounterRepository counterRepository;

  @Autowired
  public CounterServiceImpl(CounterRepository counterRepository) {
    this.counterRepository = counterRepository;
  }

  @Override
  @CacheEvict(
      value = {"counters", "counter"},
      allEntries = true)
  public Counter save(Counter counter) {
    return counterRepository.save(counter);
  }

  @Override
  @Cacheable(value = "counter", key = "#id")
  public Counter findById(Long id) {
    return counterRepository.findById(id).orElse(null);
  }

  @Override
  @Cacheable("counters")
  public Page<Counter> findAll(Pageable pageable) {
    return counterRepository.findAll(pageable);
  }

  @Override
  @CacheEvict(
      value = {"counters", "counter"},
      allEntries = true)
  public void delete(Long id) {
    counterRepository.deleteById(id);
  }

  @Override
  public int countByAgency_Merchant(com.pressing.model.Merchant merchant) {
    return counterRepository.countByAgency_Merchant(merchant);
  }
}
