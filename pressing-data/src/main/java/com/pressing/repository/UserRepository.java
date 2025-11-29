package com.pressing.repository;

import com.pressing.model.CustomUser;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<CustomUser, Long> {

  CustomUser findByUsername(@Param("name") String name);

  Collection<CustomUser> findByIsActive(@Param("isActive") boolean isActive);
}
