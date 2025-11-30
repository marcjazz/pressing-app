package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.Counter;
import com.pressing.model.MaterialPurchase;
import com.pressing.model.MaterialPurchaseDTO;
import com.pressing.service.MaterialPurchaseService;
import com.pressing.service.UserService;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
    origins = "*",
    methods = {
      RequestMethod.POST,
      RequestMethod.GET,
      RequestMethod.PUT,
      RequestMethod.DELETE,
      RequestMethod.OPTIONS
    })
@RequestMapping("api/v1/expenditures")
public class MaterialPurchaseController {

  @Autowired private MaterialPurchaseService materialPurchaseService;
  @Autowired private UserService userService;
  @Autowired private EntityMapper entityMapper;

  /**
   * Get all expenditures made or expenditures made on a given day.
   *
   * @param purchaseDate
   * @return Collection of expenditures (MaterialPurchases) or expenditures made on the given day.
   * @throws ParseException
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<MaterialPurchaseDTO>> getExpenditures(
      @RequestParam(value = "purchaseDate", required = false) String purchaseDate,
      Pageable pageable)
      throws ParseException {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Page<MaterialPurchase> materialPurchases;
    if (purchaseDate != null) {
      DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
      Date date = formatter.parse(purchaseDate);
      materialPurchases =
          materialPurchaseService.findByPurchasedDateAndCounter(date, counter, pageable);
    } else {
      materialPurchases = materialPurchaseService.findByCounter(counter, pageable);
    }

    List<MaterialPurchaseDTO> dtos =
        materialPurchases.getContent().stream()
            .map(entityMapper::toDTO)
            .collect(Collectors.toList());
    Page<MaterialPurchaseDTO> result =
        new PageImpl<>(dtos, pageable, materialPurchases.getTotalElements());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * Get expenditure with a given id.
   *
   * @param purchaseId
   * @return MaterialPurchase object or 404 if expenditure (MaterialPurchase object) is not found
   */
  @RequestMapping(
      value = "/{purchaseId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MaterialPurchaseDTO> getExpenditureById(
      @PathVariable("purchaseId") Long purchaseId) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    MaterialPurchase materialPurchase = materialPurchaseService.findById(purchaseId);
    if (materialPurchase == null || !materialPurchase.getCounter().equals(counter)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(entityMapper.toDTO(materialPurchase), HttpStatus.OK);
  }

  /**
   * Create new expenditure record.
   *
   * @param purchaseDTO
   * @return MaterialPurchase Object (created MaterialPurchase object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MaterialPurchaseDTO> createExpenditure(
      @RequestBody MaterialPurchaseDTO purchaseDTO) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    MaterialPurchase purchase = entityMapper.toEntity(purchaseDTO);
    purchase.setCounter(counter);

    purchase = materialPurchaseService.create(purchase);
    if (purchase == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(purchase), HttpStatus.CREATED);
  }

  /**
   * Update expenditure record.
   *
   * @param purchaseDTO
   * @return MaterialPurchase Object (updated MaterialPurchase object)
   */
  @RequestMapping(
      value = "/{purchaseId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<MaterialPurchaseDTO> updateExpenditure(
      @RequestBody MaterialPurchaseDTO purchaseDTO) {

    Counter counter = userService.getCurrentUser().getCounter();
    if (counter == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    MaterialPurchase existingPurchase = materialPurchaseService.findById(purchaseDTO.getId());
    if (existingPurchase == null || !existingPurchase.getCounter().equals(counter)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    MaterialPurchase purchase = entityMapper.toEntity(purchaseDTO);
    purchase.setCounter(counter);
    purchase = materialPurchaseService.update(purchase);
    if (purchase == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(purchase), HttpStatus.OK);
  }
}
