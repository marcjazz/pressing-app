package com.pressing.service.implementation;

import com.pressing.model.CleaningMaterial;
import com.pressing.model.Merchant;
import com.pressing.repository.CleaningMaterialRepository;
import com.pressing.service.CleaningMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class CleaningMaterialServiceImpl implements CleaningMaterialService {

  @Autowired private CleaningMaterialRepository cleaningMaterialRepository;

  @Override
  @Cacheable("cleaningMaterials")
  public Page<CleaningMaterial> findAll(Pageable pageable) {
    return cleaningMaterialRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "cleaningMaterialsByMerchant", key = "#merchant.id")
  public Page<CleaningMaterial> findByMerchant(Merchant merchant, Pageable pageable) {
    return cleaningMaterialRepository.findByMerchant(merchant, pageable);
  }

  @Override
  @Cacheable(value = "cleaningMaterial", key = "#id")
  public CleaningMaterial findById(Long id) {
    if (id == null) {
      return null;
    }

    CleaningMaterial cleaningMaterial = cleaningMaterialRepository.findById(id).orElse(null);
    return cleaningMaterial;
  }

  @Override
  public CleaningMaterial findByName(String name) {

    CleaningMaterial cleaningMaterial;
    cleaningMaterial = cleaningMaterialRepository.findByName(name);
    return cleaningMaterial;
  }

  @Override
  @CacheEvict(
      value = {"cleaningMaterials", "cleaningMaterial", "cleaningMaterialsByMerchant"},
      allEntries = true)
  public CleaningMaterial create(CleaningMaterial cleaningMaterial) {

    if (cleaningMaterialRepository.findByName(cleaningMaterial.getName()) == null) {
      CleaningMaterial savedCleaningMaterial;
      savedCleaningMaterial = cleaningMaterialRepository.save(cleaningMaterial);
      return savedCleaningMaterial;
    }

    return null;
  }

  @Override
  @CacheEvict(
      value = {"cleaningMaterials", "cleaningMaterial", "cleaningMaterialsByMerchant"},
      allEntries = true)
  public CleaningMaterial update(CleaningMaterial cleaningMaterial) {
    if (cleaningMaterial == null) {
      return null;
    }

    CleaningMaterial savedCleaningMaterial = cleaningMaterialRepository.save(cleaningMaterial);
    return savedCleaningMaterial;
  }

  @Override
  @CacheEvict(
      value = {"cleaningMaterials", "cleaningMaterial", "cleaningMaterialsByMerchant"},
      allEntries = true)
  public void delete(Long id) {

    CleaningMaterial cleaningMaterial = findById(id);
    if (cleaningMaterial == null) {
      return;
    }

    cleaningMaterialRepository.delete(cleaningMaterial);
  }
}
