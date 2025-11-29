package com.pressing.controller;

import com.pressing.model.Merchant;
import com.pressing.service.MerchantService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/merchants")
@CrossOrigin(origins = "*")
public class MerchantController {

  private MerchantService merchantService;

  @Autowired
  public MerchantController(MerchantService merchantService) {
    this.merchantService = merchantService;
  }

  @PostMapping
  public ResponseEntity<Merchant> save(@RequestBody Merchant merchant) {
    return new ResponseEntity<>(merchantService.save(merchant), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Merchant> findById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(merchantService.findById(id), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Merchant>> findAll() {
    return new ResponseEntity<>(merchantService.findAll(), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    merchantService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
