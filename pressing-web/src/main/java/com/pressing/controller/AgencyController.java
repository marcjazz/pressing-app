package com.pressing.controller;

import com.pressing.model.Agency;
import com.pressing.service.AgencyService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agencies")
@CrossOrigin(origins = "*")
public class AgencyController {

  private final AgencyService agencyService;
  private final com.pressing.service.UserService userService;

  @Autowired
  public AgencyController(
      AgencyService agencyService, com.pressing.service.UserService userService) {
    this.agencyService = agencyService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<Agency> save(@RequestBody Agency agency) {
    com.pressing.model.CustomUser currentUser = userService.getCurrentUser();
    if (currentUser.getMerchant() != null) {
      com.pressing.model.Merchant merchant = currentUser.getMerchant();
      if (merchant.getPlan() != null) {
        int maxAgencies = merchant.getPlan().getMaxAgencies();
        int currentAgencies = agencyService.countByMerchant(merchant);
        if (currentAgencies >= maxAgencies) {
          return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
      }
      agency.setMerchant(merchant);
    } else {
      // Handle case where user is not a merchant
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    return new ResponseEntity<>(agencyService.save(agency), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Agency> findById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(agencyService.findById(id), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Page<Agency>> findAll(Pageable pageable) {
    return new ResponseEntity<>(agencyService.findAll(pageable), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    agencyService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
