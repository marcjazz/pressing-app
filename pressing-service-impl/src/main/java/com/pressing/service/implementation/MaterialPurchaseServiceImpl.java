package com.pressing.service.implementation;

import com.pressing.model.Counter;
import com.pressing.model.MaterialPurchase;
import com.pressing.repository.MaterialPurchaseRepository;
import com.pressing.service.MaterialPurchaseService;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class MaterialPurchaseServiceImpl implements MaterialPurchaseService {

  @Autowired private MaterialPurchaseRepository materialpurchaserepository;

  @Override
  @Cacheable("materialPurchases")
  public Page<MaterialPurchase> findAll(Pageable pageable) {
    return materialpurchaserepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "materialPurchasesByCounter", key = "#counter.id")
  public Page<MaterialPurchase> findByCounter(Counter counter, Pageable pageable) {
    return materialpurchaserepository.findByCounter(counter, pageable);
  }

  @Override
  @Cacheable(value = "materialPurchasesByDateAndCounter", key = "#date.toString() + '-' + #counter.id")
  public Page<MaterialPurchase> findByPurchasedDateAndCounter(Date date, Counter counter, Pageable pageable) {
    return materialpurchaserepository.findByPurchasedDateAndCounter(date, counter, pageable);
  }

  @Override
  @Cacheable(value = "materialPurchase", key = "#id")
  public MaterialPurchase findById(Long id) {
    if (id == null) {
      return null;
    }

    MaterialPurchase materialPurchase = materialpurchaserepository.findById(id).orElse(null);
    return materialPurchase;
  }

  @Override
  public Collection<MaterialPurchase> findByPurchasDate(Date purchaseDate) {

    Collection<MaterialPurchase> materialPurchases =
        materialpurchaserepository.findByPurchasedDate(purchaseDate);
    return materialPurchases;
  }

  @Override
  public Collection<MaterialPurchase> findByCleaningMaterialId(Long id) {

    Collection<MaterialPurchase> materialPurchases =
        materialpurchaserepository.findByCleaningMaterialId(id);
    return materialPurchases;
  }

  @Override
  @CacheEvict(value = {"materialPurchases", "materialPurchase", "materialPurchasesByCounter", "materialPurchasesByDateAndCounter"}, allEntries = true)
  public MaterialPurchase create(MaterialPurchase purchase) {
    if (purchase == null) {
      return null;
    }

    MaterialPurchase savedPurchase = materialpurchaserepository.save(purchase);
    return savedPurchase;
  }

  @Override
  @CacheEvict(value = {"materialPurchases", "materialPurchase", "materialPurchasesByCounter", "materialPurchasesByDateAndCounter"}, allEntries = true)
  public MaterialPurchase update(MaterialPurchase purchase) {
    if (purchase == null) {
      return null;
    }

    MaterialPurchase updatedPurchase = materialpurchaserepository.save(purchase);
    return updatedPurchase;
  }
}
