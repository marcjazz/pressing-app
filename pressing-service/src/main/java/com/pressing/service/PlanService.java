package com.pressing.service;

import com.pressing.model.Plan;
import java.util.List;

public interface PlanService {
  Plan create(Plan plan);

  List<Plan> findAll();

  Plan findById(Integer id);

  void delete(Integer id);
}
