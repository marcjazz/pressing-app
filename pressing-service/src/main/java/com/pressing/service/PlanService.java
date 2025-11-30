package com.pressing.service;

import com.pressing.model.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlanService {
  Plan create(Plan plan);

  Page<Plan> findAll(Pageable pageable);

  Plan findById(Integer id);

  void delete(Integer id);
}
