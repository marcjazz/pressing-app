package com.pressing.repository;

import com.pressing.model.CustomUser;
import com.pressing.model.Merchant;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

  CustomUser findByUsername(@Param("name") String name);

  Collection<CustomUser> findByIsActive(@Param("isActive") boolean isActive);

  Page<CustomUser> findByMerchant(Merchant merchant, Pageable pageable);

  Page<CustomUser> findByMerchantAndIsActive(Merchant merchant, boolean isActive, Pageable pageable);
}
