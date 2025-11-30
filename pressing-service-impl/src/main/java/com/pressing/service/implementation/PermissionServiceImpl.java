package com.pressing.service.implementation;

import com.pressing.model.Permission;
import com.pressing.repository.PermissionRepository;
import com.pressing.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured("ROLE_ADMINISTRATION")
public class PermissionServiceImpl implements PermissionService {

  @Autowired private PermissionRepository permissionRepository;

  @Override
  @Cacheable("permissions")
  public Page<Permission> findAll(Pageable pageable) {
    return permissionRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "permission", key = "#id")
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
  @CacheEvict(
      value = {"permissions", "permission"},
      allEntries = true)
  public Permission create(Permission permission) {
    if (permission == null) {
      return null;
    }

    Permission savedPermission = permissionRepository.save(permission);
    return savedPermission;
  }

  @Override
  @CacheEvict(
      value = {"permissions", "permission"},
      allEntries = true)
  public Permission update(Permission permission) {
    if (permission == null || permission.getId() == null) {
      return null;
    }

    Permission updatedPermission = permissionRepository.save(permission);
    return updatedPermission;
  }
}
