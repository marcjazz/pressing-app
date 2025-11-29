package com.pressing.service.implementation;

import com.pressing.model.CustomUser;
import com.pressing.model.Merchant;
import com.pressing.repository.UserRepository;
import com.pressing.service.UserService;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired private UserRepository userRepository;


  @Override
  @Secured("ROLE_ADMINISTRATION")
  @Cacheable("users")
  public Page<CustomUser> findAll(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "user", key = "#id")
  public CustomUser findById(Long id) {
    if (id == null) {
      return null;
    }

    CustomUser user = userRepository.findById(id).orElse(null);
    return user;
  }

  @Override
  public CustomUser findByUserName(String username) {

    CustomUser user = userRepository.findByUsername(username);
    return user;
  }

  @Override
  public Collection<CustomUser> findByIsActive(boolean isActive) {
    return userRepository.findByIsActive(isActive);
  }

  @Override
  @Cacheable(value = "usersByMerchant", key = "#merchant.id")
  public Page<CustomUser> findByMerchant(Merchant merchant, Pageable pageable) {
    return userRepository.findByMerchant(merchant, pageable);
  }

  @Override
  @Cacheable(value = "usersByMerchantAndActive", key = "#merchant.id + '-' + #isActive")
  public Page<CustomUser> findByMerchantAndIsActive(Merchant merchant, boolean isActive, Pageable pageable) {
    return userRepository.findByMerchantAndIsActive(merchant, isActive, pageable);
  }

  @Override
  @Secured("ROLE_ADMINISTRATION")
  @CacheEvict(value = {"users", "user", "usersByMerchant", "usersByMerchantAndActive"}, allEntries = true)
  public Optional<CustomUser> create(CustomUser user) {

    if (findByUserName(user.getUsername()) != null) {
      return Optional.empty();
    }
    user.setPassword(user.getPassword());
    CustomUser savedUser = userRepository.save(user);
    return Optional.of(savedUser);
  }

  @Override
  @CacheEvict(value = {"users", "user", "usersByMerchant", "usersByMerchantAndActive"}, allEntries = true)
  public CustomUser update(CustomUser user) {

    user.setPassword(user.getPassword());
    CustomUser savedUser = userRepository.save(user);
    return savedUser;
  }

  @Override
  @Secured("ROLE_ADMINISTRATION")
  @CacheEvict(value = {"users", "user", "usersByMerchant", "usersByMerchantAndActive"}, allEntries = true)
  public void deactivate(Long id) {

    CustomUser user = findById(id);
    user.setActive(false);
  }

  @Override
  public CustomUser getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      String username = authentication.getName();
      return findByUserName(username);
    }
    return null;
  }
}
