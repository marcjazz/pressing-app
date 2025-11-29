package com.pressing.service.implementation;

import com.pressing.model.Category;
import com.pressing.repository.CategoryRepository;
import com.pressing.service.CategoryService;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class CategoryServiceImpl implements CategoryService {

  @Autowired private CategoryRepository categoryRepository;

  @Override
  @Cacheable("categories")
  public Page<Category> findAll(Pageable pageable) {
    return categoryRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "category", key = "#id")
  public Category findById(Long id) {
    if (id == null) {
      return null;
    }

    Category category = categoryRepository.findById(id).orElse(null);
    return category;
  }

  @Override
  public Category findByName(String name) {

    Category category = categoryRepository.findByName(name);
    return category;
  }

  @Override
  @CacheEvict(value = "categories", allEntries = true)
  public Category create(Category category) {
    if (category == null) {
      return null;
    }

    Long id = category.getId();
    if (id == null) {
      return categoryRepository.save(category);
    }

    if (categoryRepository.existsById(id)) {
      return null;
    }

    return categoryRepository.save(category);
  }

  @Override
  @CacheEvict(value = {"categories", "category"}, allEntries = true)
  public Category update(Category category) {

    if (categoryRepository.findByName(category.getName()) == null) {

      Category savedCategory = categoryRepository.save(category);
      return savedCategory;
    }

    return null;
  }

  @Override
  @CacheEvict(value = {"categories", "category"}, allEntries = true)
  public void delete(Long id) {

    Category category = findById(id);
    if (category == null) {
      return;
    }

    categoryRepository.delete(category);
  }
}
