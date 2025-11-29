package com.pressing.controller;

import com.pressing.model.Counter;
import com.pressing.service.CounterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/counters")
@CrossOrigin(origins = "*")
public class CounterController {

  private final CounterService counterService;
  private final com.pressing.service.UserService userService;

  @Autowired
  public CounterController(
      CounterService counterService, com.pressing.service.UserService userService) {
    this.counterService = counterService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<Counter> save(@RequestBody Counter counter) {
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
    return new ResponseEntity<>(counterService.save(counter), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Counter> findById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(counterService.findById(id), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Counter>> findAll() {
    return new ResponseEntity<>(counterService.findAll(), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    counterService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
