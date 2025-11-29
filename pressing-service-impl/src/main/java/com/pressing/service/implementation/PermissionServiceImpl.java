package com.pressing.service.implementation;

import com.pressing.model.Permission;
import com.pressing.repository.PermissionRepository;
import com.pressing.service.PermissionService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ROLE_ADMINISTRATION")
public class PermissionServiceImpl implements PermissionService {

  @Autowired private PermissionRepository permissionRepository;

  @Override
  public Collection<Permission> findAll() {

    Collection<Permission> permissions = permissionRepository.findAll();
    return permissions;
  }

  @Override
  public Permission findById(Long id) {
    if (id == null) {
      return null;
    }

    Permission permission = permissionRepository.findById(id).orElse(null);
    return permission;
  }

  @Override
  public Permission findByName(String name) {

    Permission permission = permissionRepository.findByName(name);
    return permission;
  }

  @Override
  public Permission create(Permission permission) {
    if (permission == null) {
      return null;
    }

    Permission savedPermission = permissionRepository.save(permission);
    return savedPermission;
  }

  @Override
  public Permission update(Permission permission) {
    if (permission == null || permission.getId() == null) {
      return null;
    }

    Permission updatedPermission = permissionRepository.save(permission);
    return updatedPermission;
  }
}
