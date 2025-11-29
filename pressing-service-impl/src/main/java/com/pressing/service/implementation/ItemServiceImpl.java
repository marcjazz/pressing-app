package com.pressing.service.implementation;

import com.pressing.model.Category;
import com.pressing.model.Item;
import com.pressing.model.Merchant;
import com.pressing.repository.CategoryRepository;
import com.pressing.repository.ItemRepository;
import com.pressing.service.ItemService;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
@Secured({"ROLE_MANAGEMENT", "ROLE_ADMINISTRATION"})
public class ItemServiceImpl implements ItemService {

  @Autowired private ItemRepository itemRepository;

  @Autowired private CategoryRepository categoryRepository;

  @Override
  @Cacheable("items")
  public Page<Item> findAll(Pageable pageable) {
    return itemRepository.findAll(pageable);
  }

  @Override
  @Cacheable(value = "itemsByMerchant", key = "#merchant.id")
  public Page<Item> findByMerchant(Merchant merchant, Pageable pageable) {
    return itemRepository.findByMerchant(merchant, pageable);
  }

  @Override
  @Cacheable(value = "item", key = "#id")
  public Item findById(Long id) {
    if (id == null) {
      return null;
    }

    Item item = itemRepository.findById(id).orElse(null);
    return item;
  }

  @Override
  public Item findByName(String name) {

    Item item = itemRepository.findByName(name);
    return item;
  }

  @Override
  @CacheEvict(value = {"items", "item", "itemsByMerchant"}, allEntries = true)
  public Item create(Item item) {

    if (itemRepository.findByName(item.getName()) == null) {
      Item savedItem = itemRepository.save(item);
      return savedItem;
    }

    return null;
  }

  @Override
  @CacheEvict(value = {"items", "item", "itemsByMerchant"}, allEntries = true)
  public Item update(Item item) {
    if (item == null) {
      return null;
    }

    Item updatedItem = itemRepository.save(item);
    return updatedItem;
  }

  @Override
  @CacheEvict(value = {"items", "item", "itemsByMerchant"}, allEntries = true)
  public void delete(Long id) {

    Item item = findById(id);
    if (item == null) {
      return;
    }

    itemRepository.delete(item);
  }

  @Override
  public Collection<Item> findCategoryItems(Long categoryId) {
    if (categoryId == null) {
      return null;
    }

    Category category = categoryRepository.findById(categoryId).orElse(null);
    Collection<Item> items = itemRepository.findByCategory(category);
    return items;
  }
}
