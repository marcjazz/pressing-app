package com.pressing.service.implementation;

import com.pressing.model.CleaningMaterial;
import com.pressing.repository.CleaningMaterialRepository;
import com.pressing.service.CleaningMaterialService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class CleaningMaterialServiceImpl implements CleaningMaterialService {

  @Autowired private CleaningMaterialRepository cleaningMaterialRepository;

  @Override
  public Collection<CleaningMaterial> findAll() {

    Collection<CleaningMaterial> cleaningMaterials;
    cleaningMaterials = cleaningMaterialRepository.findAll();
    return cleaningMaterials;
  }

  @Override
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
  public CleaningMaterial create(CleaningMaterial cleaningMaterial) {

    if (cleaningMaterialRepository.findByName(cleaningMaterial.getName()) == null) {
      CleaningMaterial savedCleaningMaterial;
      savedCleaningMaterial = cleaningMaterialRepository.save(cleaningMaterial);
      return savedCleaningMaterial;
    }

    return null;
  }

  @Override
  public CleaningMaterial update(CleaningMaterial cleaningMaterial) {
    if (cleaningMaterial == null) {
      return null;
    }

    CleaningMaterial savedCleaningMaterial = cleaningMaterialRepository.save(cleaningMaterial);
    return savedCleaningMaterial;
  }

  @Override
  public void delete(Long id) {

    CleaningMaterial cleaningMaterial = findById(id);
    if (cleaningMaterial == null) {
      return;
    }

    cleaningMaterialRepository.delete(cleaningMaterial);
  }
}
