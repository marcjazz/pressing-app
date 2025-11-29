package com.pressing.controller;

import com.pressing.model.Plan;
import com.pressing.service.PlanService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plans")
@CrossOrigin(origins = "*")
public class PlanController {

  private final PlanService planService;

  @Autowired
  public PlanController(PlanService planService) {
    this.planService = planService;
  }

  @PostMapping
  public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
    Plan newPlan = planService.create(plan);
    return new ResponseEntity<>(newPlan, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Plan>> getAllPlans() {
    List<Plan> plans = planService.findAll();
    return new ResponseEntity<>(plans, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Plan> getPlanById(@PathVariable Integer id) {
    Plan plan = planService.findById(id);
    if (plan != null) {
      return new ResponseEntity<>(plan, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletePlan(@PathVariable Integer id) {
    planService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
