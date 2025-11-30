package com.pressing.service.implementation;

import com.pressing.model.Merchant;
import com.pressing.repository.MerchantRepository;
import com.pressing.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

  private MerchantRepository merchantRepository;

  @Autowired
  public MerchantServiceImpl(MerchantRepository merchantRepository) {
    this.merchantRepository = merchantRepository;
  }

  @Override
  @CacheEvict(
      value = {"merchants", "merchant"},
      allEntries = true)
  public Merchant save(Merchant merchant) {
    return merchantRepository.save(merchant);
  }

  @Override
  @Cacheable(value = "merchant", key = "#id")
  public Merchant findById(Long id) {
    return merchantRepository.findById(id).orElse(null);
  }

  @Override
  @Cacheable("merchants")
  public Page<Merchant> findAll(Pageable pageable) {
    return merchantRepository.findAll(pageable);
  }

  @Override
  @CacheEvict(
      value = {"merchants", "merchant"},
      allEntries = true)
  public void delete(Long id) {
    merchantRepository.deleteById(id);
  }
}
