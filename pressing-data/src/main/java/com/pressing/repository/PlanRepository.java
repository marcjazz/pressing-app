package com.pressing.repository;

import com.pressing.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Integer> {}
