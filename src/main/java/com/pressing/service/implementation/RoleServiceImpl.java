package com.pressing.service.implementation;

import com.pressing.model.Role;
import com.pressing.repository.RoleRepository;
import com.pressing.service.RoleService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ROLE_ADMINISTRATION")
public class RoleServiceImpl implements RoleService {

  @Autowired private RoleRepository roleRepository;

  @Override
  public Collection<Role> findAll() {

    Collection<Role> roles = roleRepository.findAll();
    return roles;
  }

  @Override
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
  public Role create(Role role) {
    if (role == null) {
      return null;
    }

    Role savedRole = roleRepository.save(role);
    return savedRole;
  }

  @Override
  public Role update(Role role) {
    if (role == null || role.getId() == null) {
      return null;
    }

    Role updatedRole = roleRepository.save(role);
    return updatedRole;
  }
}
