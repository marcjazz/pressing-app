package com.pressing.service.implementation;

import com.pressing.model.Plan;
import com.pressing.repository.PlanRepository;
import com.pressing.service.PlanService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanServiceImpl implements PlanService {

  private final PlanRepository planRepository;

  @Autowired
  public PlanServiceImpl(PlanRepository planRepository) {
    this.planRepository = planRepository;
  }

  @Override
  public Plan create(Plan plan) {
    return planRepository.save(plan);
  }

  @Override
  public List<Plan> findAll() {
    return planRepository.findAll();
  }

  @Override
  public Plan findById(Integer id) {
    return planRepository.findById(id).orElse(null);
  }

  @Override
  public void delete(Integer id) {
    planRepository.deleteById(id);
  }
}
