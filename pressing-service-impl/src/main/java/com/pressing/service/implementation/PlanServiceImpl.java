package com.pressing.service.implementation;

import com.pressing.model.Plan;
import com.pressing.repository.PlanRepository;
import com.pressing.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceImpl implements PlanService {

  private final PlanRepository planRepository;

  @Autowired
  public PlanServiceImpl(PlanRepository planRepository) {
    this.planRepository = planRepository;
  }

  @Override
  @CacheEvict(
      value = {"plans", "plan"},
      allEntries = true)
  public Plan create(Plan plan) {
    return planRepository.save(plan);
  }

  @Override
  @Cacheable("plans")
  public Page<Plan> findAll(Pageable pageable) {
    return planRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "plan", key = "#id")
  public Plan findById(Integer id) {
    return planRepository.findById(id).orElse(null);
  }

  @Override
  @CacheEvict(
      value = {"plans", "plan"},
      allEntries = true)
  public void delete(Integer id) {
    planRepository.deleteById(id);
  }
}
