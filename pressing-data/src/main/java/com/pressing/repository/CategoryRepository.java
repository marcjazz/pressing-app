package com.pressing.repository;

import com.pressing.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

  Category findByName(@Param("name") String name);
}
