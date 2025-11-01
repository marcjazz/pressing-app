package com.pressing.service.implementation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.pressing.model.Category;
import com.pressing.repository.CategoryRepository;
import com.pressing.service.CategoryService;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Collection<Category> findAll() {
		
		Collection<Category> categories = categoryRepository.findAll();
		return categories;
	}

	@Override
	public Category findById(Long id) {
		
		Category category = categoryRepository.findById(id).orElse(null);
		return category;
	}

	@Override
	public Category findByName(String name) {
		
		Category category = categoryRepository.findByName(name);
		return category;	
	}

	@Override
	public Category create(Category category) {
		
		if ( categoryRepository.existsById(category.getId()) ) {
			return null;
		}
		
		Category savedCategory = categoryRepository.save(category);
		return savedCategory;
	}

	@Override
	public Category update(Category category) {
		
		if ( categoryRepository.findByName(category.getName()) == null ) {

			Category savedCategory = categoryRepository.save(category);
			return savedCategory;
		}

		return null;
	}

	@Override
	public void delete(Long id) {
		
		Category category = findById(id);
		if (category == null) {
			return;
		}
		
		categoryRepository.delete(category);
	}

}
