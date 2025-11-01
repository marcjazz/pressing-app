package com.pressing.service.implementation;

import com.pressing.model.MaterialPurchase;
import com.pressing.repository.MaterialPurchaseRepository;
import com.pressing.service.MaterialPurchaseService;
import java.util.Collection;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class MaterialPurchaseServiceImpl implements MaterialPurchaseService {

  @Autowired private MaterialPurchaseRepository materialpurchaserepository;

  @Override
  public Collection<MaterialPurchase> findAll() {

    Collection<MaterialPurchase> materialPurchases = materialpurchaserepository.findAll();
    return materialPurchases;
  }

  @Override
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
  public MaterialPurchase create(MaterialPurchase purchase) {
    if (purchase == null) {
      return null;
    }

    MaterialPurchase savedPurchase = materialpurchaserepository.save(purchase);
    return savedPurchase;
  }

  @Override
  public MaterialPurchase update(MaterialPurchase purchase) {
    if (purchase == null) {
      return null;
    }

    MaterialPurchase updatedPurchase = materialpurchaserepository.save(purchase);
    return updatedPurchase;
  }
}
