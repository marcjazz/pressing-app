package com.pressing.service.implementation;

import com.pressing.model.Category;
import com.pressing.model.Item;
import com.pressing.repository.CategoryRepository;
import com.pressing.repository.ItemRepository;
import com.pressing.service.ItemService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class ItemServiceImpl implements ItemService {

  @Autowired private ItemRepository itemRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Override
  public Collection<Item> findAll() {

    Collection<Item> items = itemRepository.findAll();
    return items;
  }

  @Override
  public Item findById(Long id) {

    Item item = itemRepository.findById(id).orElse(null);
    return item;
  }

  @Override
  public Item findByName(String name) {

    Item item = itemRepository.findByName(name);
    return item;
  }

  @Override
  public Item create(Item item) {

    if (itemRepository.findByName(item.getName()) == null) {
      Item savedItem = itemRepository.save(item);
      return savedItem;
    }

    return null;
  }

  @Override
  public Item update(Item item) {

    Item updatedItem = itemRepository.save(item);
    return updatedItem;
  }

  @Override
  public void delete(Long id) {

    Item item = findById(id);
    if (item == null) {
      return;
    }

    itemRepository.delete(item);
  }

  @Override
  public Collection<Item> findCategoryItems(Long categoryId) {

    Category category = categoryRepository.findById(categoryId).orElse(null);
    Collection<Item> items = itemRepository.findByCategory(category);
    return items;
  }
}
