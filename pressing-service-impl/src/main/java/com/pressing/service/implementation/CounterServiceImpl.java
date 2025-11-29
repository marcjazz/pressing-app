package com.pressing.service.implementation;

import com.pressing.model.Counter;
import com.pressing.repository.CounterRepository;
import com.pressing.service.CounterService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
  public Counter save(Counter counter) {
    return counterRepository.save(counter);
  }

  @Override
  public Counter findById(Long id) {
    return counterRepository.findById(id).orElse(null);
  }

  @Override
  public List<Counter> findAll() {
    return counterRepository.findAll();
  }

  @Override
  public void delete(Long id) {
    counterRepository.deleteById(id);
  }

  @Override
  public int countByAgency_Merchant(com.pressing.model.Merchant merchant) {
    return counterRepository.countByAgency_Merchant(merchant);
  }
}
