package com.pressing.service;

import com.pressing.model.Counter;
import com.pressing.model.MaterialPurchase;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service that provides CRUD operations for material purchases */
public interface MaterialPurchaseService {

  /**
   * Get all cleaning material purchases in the system.
   *
   * @return Collection of all material purchases existing in the system
   */
  public Page<MaterialPurchase> findAll(Pageable pageable);

  /**
   * Find a material purchase by Id.
   *
   * @param purchaseId
   * @return MaterialPurchase object if found, else return null
   */
  public MaterialPurchase findById(Long id);

  /**
   * Find a purchase by purchase date.
   *
   * @param purchasedDate
   * @return Collection of purchases
   */
  public Collection<MaterialPurchase> findByPurchasDate(Date purchaseDate);

  public Page<MaterialPurchase> findByCounter(Counter counter, Pageable pageable);

  public Page<MaterialPurchase> findByPurchasedDateAndCounter(
      Date date, Counter counter, Pageable pageable);

  /**
   * Find a purchase by cleaning material.
   *
   * @param materialId
   * @return Collection of purchases
   */
  public Collection<MaterialPurchase> findByCleaningMaterialId(Long materialId);

  /**
   * Create a new purchase.
   *
   * @param purchase
   * @return MatetrialPurchase object (created purchase transaction)
   */
  public MaterialPurchase create(MaterialPurchase purchase);

  /**
   * Update an existing purchase.
   *
   * @param purchase the updated purchase's record
   * @return MatetrialPurchase object (updated purchase transaction)
   */
  public MaterialPurchase update(MaterialPurchase purchase);
}
