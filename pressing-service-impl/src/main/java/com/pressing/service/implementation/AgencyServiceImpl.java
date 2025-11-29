package com.pressing.service.implementation;

import com.pressing.model.Agency;
import com.pressing.repository.AgencyRepository;
import com.pressing.service.AgencyService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AgencyServiceImpl implements AgencyService {

  private AgencyRepository agencyRepository;

  @Autowired
  public AgencyServiceImpl(AgencyRepository agencyRepository) {
    this.agencyRepository = agencyRepository;
  }

  @Override
  public Agency save(Agency agency) {
    return agencyRepository.save(agency);
  }

  @Override
  public Agency findById(Long id) {
    return agencyRepository.findById(id).orElse(null);
  }

  @Override
  public List<Agency> findAll() {
    return agencyRepository.findAll();
  }

  @Override
  public void delete(Long id) {
    agencyRepository.deleteById(id);
  }

  @Override
  public int countByMerchant(com.pressing.model.Merchant merchant) {
    return agencyRepository.countByMerchant(merchant);
  }
}
