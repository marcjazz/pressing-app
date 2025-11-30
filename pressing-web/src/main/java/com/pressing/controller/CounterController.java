package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.Counter;
import com.pressing.model.CounterDTO;
import com.pressing.service.CounterService;
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
@RequestMapping("/api/counters")
@CrossOrigin(origins = "*")
public class CounterController {

  private final CounterService counterService;
  private final com.pressing.service.UserService userService;
  private final EntityMapper entityMapper;

  @Autowired
  public CounterController(
      CounterService counterService,
      com.pressing.service.UserService userService,
      EntityMapper entityMapper) {
    this.counterService = counterService;
    this.userService = userService;
    this.entityMapper = entityMapper;
  }

  @PostMapping
  public ResponseEntity<CounterDTO> save(@RequestBody CounterDTO counterDTO) {
    Counter counter = entityMapper.toEntity(counterDTO);
    com.pressing.model.CustomUser currentUser = userService.getCurrentUser();
    if (currentUser.getMerchant() != null) {
      com.pressing.model.Merchant merchant = currentUser.getMerchant();
      if (merchant.getPlan() != null) {
        int maxCounters = merchant.getPlan().getMaxCounters();
        int currentCounters = counterService.countByAgency_Merchant(merchant);
        if (currentCounters >= maxCounters) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      }
    } else {
      // Handle case where user is not a merchant
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return new ResponseEntity<>(
        entityMapper.toDTO(counterService.save(counter)), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CounterDTO> findById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(entityMapper.toDTO(counterService.findById(id)), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Page<CounterDTO>> findAll(Pageable pageable) {
    Page<Counter> counters = counterService.findAll(pageable);
    List<CounterDTO> counterDTOs =
        counters.getContent().stream().map(entityMapper::toDTO).collect(Collectors.toList());
    Page<CounterDTO> result = new PageImpl<>(counterDTOs, pageable, counters.getTotalElements());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    counterService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
