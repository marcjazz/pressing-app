package com.pressing.repository;

import com.pressing.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

  Permission findByName(@Param("name") String name);
}
