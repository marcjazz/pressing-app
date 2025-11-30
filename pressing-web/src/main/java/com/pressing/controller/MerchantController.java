package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.Merchant;
import com.pressing.model.MerchantDTO;
import com.pressing.service.MerchantService;
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
@RequestMapping("/api/merchants")
@CrossOrigin(origins = "*")
public class MerchantController {

  private MerchantService merchantService;
  private EntityMapper entityMapper;

  @Autowired
  public MerchantController(MerchantService merchantService, EntityMapper entityMapper) {
    this.merchantService = merchantService;
    this.entityMapper = entityMapper;
  }

  @PostMapping
  public ResponseEntity<MerchantDTO> save(@RequestBody MerchantDTO merchantDTO) {
    Merchant merchant = entityMapper.toEntity(merchantDTO);
    merchant = merchantService.save(merchant);
    return new ResponseEntity<>(entityMapper.toDTO(merchant), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<MerchantDTO> findById(@PathVariable("id") Long id) {
    Merchant merchant = merchantService.findById(id);
    return new ResponseEntity<>(entityMapper.toDTO(merchant), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Page<MerchantDTO>> findAll(Pageable pageable) {
    Page<Merchant> merchants = merchantService.findAll(pageable);
    List<MerchantDTO> dtos =
        merchants.getContent().stream().map(entityMapper::toDTO).collect(Collectors.toList());
    Page<MerchantDTO> result = new PageImpl<>(dtos, pageable, merchants.getTotalElements());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    merchantService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
