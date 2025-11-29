package com.pressing.service;

import com.pressing.model.Item;
import com.pressing.model.Merchant;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service that provides CRUD operations for items */
public interface ItemService {

  /**
   * Get all items in the system.
   *
   * @return Collection of items
   */
  public Page<Item> findAll(Pageable pageable);

  /**
   * Find an item by Id.
   *
   * @param id
   * @return Item object if found, else return null
   */
  public Item findById(Long id);

  /**
   * Find an item by name.
   *
   * @param name
   * @return Item object if found, else return null
   */
  public Item findByName(String name);

  /**
   * Find a items with same category.
   *
   * @param id
   * @return Collection of items
   */
  public Collection<Item> findCategoryItems(Long id);

  public Page<Item> findByMerchant(Merchant merchant, Pageable pageable);

  /**
   * Create new Item.
   *
   * @param item
   * @return Item object (created item object )
   */
  public Item create(Item item);

  /**
   * Update an existing item's information.
   *
   * @param item
   * @return Item object (updated item object)
   */
  public Item update(Item item);

  /**
   * Delete a item from the system.
   *
   * @param id item's id
   */
  public void delete(Long id);
}
