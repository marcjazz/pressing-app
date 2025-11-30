package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.Plan;
import com.pressing.model.PlanDTO;
import com.pressing.service.PlanService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/plans")
@CrossOrigin(origins = "*")
public class PlanController {

  private final PlanService planService;
  private final EntityMapper entityMapper;

  @Autowired
  public PlanController(PlanService planService, EntityMapper entityMapper) {
    this.planService = planService;
    this.entityMapper = entityMapper;
  }

  @PostMapping
  public ResponseEntity<PlanDTO> createPlan(@RequestBody PlanDTO planDTO) {
    Plan plan = entityMapper.toEntity(planDTO);
    Plan newPlan = planService.create(plan);
    return new ResponseEntity<>(entityMapper.toDTO(newPlan), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Page<PlanDTO>> getAllPlans(Pageable pageable) {
    Page<Plan> plans = planService.findAll(pageable);
    List<PlanDTO> dtos =
        plans.getContent().stream().map(entityMapper::toDTO).collect(Collectors.toList());
    Page<PlanDTO> result = new PageImpl<>(dtos, pageable, plans.getTotalElements());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PlanDTO> getPlanById(@PathVariable Integer id) {
    Plan plan = planService.findById(id);
    if (plan != null) {
      return new ResponseEntity<>(entityMapper.toDTO(plan), HttpStatus.OK);
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
