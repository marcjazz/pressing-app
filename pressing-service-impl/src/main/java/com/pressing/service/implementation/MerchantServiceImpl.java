package com.pressing.service.implementation;

import com.pressing.model.Merchant;
import com.pressing.repository.MerchantRepository;
import com.pressing.service.MerchantService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
  public Merchant save(Merchant merchant) {
    return merchantRepository.save(merchant);
  }

  @Override
  public Merchant findById(Long id) {
    return merchantRepository.findById(id).orElse(null);
  }

  @Override
  public List<Merchant> findAll() {
    return merchantRepository.findAll();
  }

  @Override
  public void delete(Long id) {
    merchantRepository.deleteById(id);
  }
}
