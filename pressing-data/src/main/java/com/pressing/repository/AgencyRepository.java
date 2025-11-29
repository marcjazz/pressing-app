package com.pressing.repository;

import com.pressing.model.Agency;
import com.pressing.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
  int countByMerchant(Merchant merchant);
}
