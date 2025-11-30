package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.Agency;
import com.pressing.model.AgencyDTO;
import com.pressing.service.AgencyService;
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
@RequestMapping("/api/agencies")
@CrossOrigin(origins = "*")
public class AgencyController {

  private final AgencyService agencyService;
  private final com.pressing.service.UserService userService;
  private final EntityMapper entityMapper;

  @Autowired
  public AgencyController(
      AgencyService agencyService,
      com.pressing.service.UserService userService,
      EntityMapper entityMapper) {
    this.agencyService = agencyService;
    this.userService = userService;
    this.entityMapper = entityMapper;
  }

  @PostMapping
  public ResponseEntity<AgencyDTO> save(@RequestBody AgencyDTO agencyDTO) {
    Agency agency = entityMapper.toEntity(agencyDTO);
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
    return new ResponseEntity<>(
        entityMapper.toDTO(agencyService.save(agency)), HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AgencyDTO> findById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(
        entityMapper.toDTO(agencyService.findById(id)), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Page<AgencyDTO>> findAll(Pageable pageable) {
    Page<Agency> agencies = agencyService.findAll(pageable);
    List<AgencyDTO> agencyDTOs =
        agencies.getContent().stream()
            .map(entityMapper::toDTO)
            .collect(Collectors.toList());
    Page<AgencyDTO> result =
        new PageImpl<>(agencyDTOs, pageable, agencies.getTotalElements());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
    agencyService.delete(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
