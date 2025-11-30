package com.pressing.service;

import com.pressing.model.CleaningMaterial;
import com.pressing.model.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service that provides CRUD operations for cleaning materials */
public interface CleaningMaterialService {

  /**
   * Get all cleaning materials.
   *
   * @return Collection of cleaning materials
   */
  public Page<CleaningMaterial> findAll(Pageable pageable);

  public Page<CleaningMaterial> findByMerchant(Merchant merchant, Pageable pageable);

  /**
   * Find a cleaning material by Id.
   *
   * @param id
   * @return CleaningMaterial object if found else return null
   */
  public CleaningMaterial findById(Long id);

  /**
   * Find a cleaning material by name.
   *
   * @param name
   * @return CleaningMaterial object if found else return null
   */
  public CleaningMaterial findByName(String name);

  /**
   * Create a new cleaning material.
   *
   * @param cleaningMaterial
   * @return CleaningMaterial object (created CleaningMaterial object)
   */
  public CleaningMaterial create(CleaningMaterial cleaningMaterial);

  /**
   * Update an existing cleaningMaterial object.
   *
   * @param cleaningMaterial
   * @return CleaningMaterial object (updated cleaning material object)
   */
  public CleaningMaterial update(CleaningMaterial cleaningMaterial);

  /**
   * Delete a CleaningMaterial from the system
   *
   * @param id
   */
  public void delete(Long id);
}
