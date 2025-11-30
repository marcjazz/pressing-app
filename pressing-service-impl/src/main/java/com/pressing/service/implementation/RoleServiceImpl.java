package com.pressing.service.implementation;

import com.pressing.model.Role;
import com.pressing.repository.RoleRepository;
import com.pressing.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ROLE_ADMINISTRATION")
public class RoleServiceImpl implements RoleService {

  @Autowired private RoleRepository roleRepository;

  @Override
  @Cacheable("roles")
  public Page<Role> findAll(Pageable pageable) {
    return roleRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "role", key = "#id")
  public Role findById(Long id) {
    if (id == null) {
      return null;
    }

    Role role = roleRepository.findById(id).orElse(null);
    return role;
  }

  @Override
  public Role findByName(String name) {

    Role role = roleRepository.findByName(name);
    return role;
  }

  @Override
  @CacheEvict(
      value = {"roles", "role"},
      allEntries = true)
  public Role create(Role role) {
    if (role == null) {
      return null;
    }

    Role savedRole = roleRepository.save(role);
    return savedRole;
  }

  @Override
  @CacheEvict(
      value = {"roles", "role"},
      allEntries = true)
  public Role update(Role role) {
    if (role == null || role.getId() == null) {
      return null;
    }

    Role updatedRole = roleRepository.save(role);
    return updatedRole;
  }
}
