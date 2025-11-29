package com.pressing.controller;

import com.pressing.model.Item;
import com.pressing.model.Merchant;
import com.pressing.service.ItemService;
import com.pressing.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(
    origins = "*",
    methods = {
      RequestMethod.POST,
      RequestMethod.GET,
      RequestMethod.PUT,
      RequestMethod.DELETE,
      RequestMethod.OPTIONS
    })
@RequestMapping("api/v1/items")
public class ItemController {

  @Autowired private ItemService itemService;
  @Autowired private UserService userService;

  /**
   * Get all items or item with a given name.
   *
   * @param itemName
   * @return Collection of items or item with the given name
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Item>> getItems(
      @RequestParam(value = "itemName", required = false) String itemName, Pageable pageable) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    if (itemName != null) {
      Collection<Item> items = new ArrayList<>();
      Item item = itemService.findByName(itemName);
      if (item != null && item.getMerchant().equals(merchant)) {
        items.add(item);
      }
      Page<Item> singleResult = new PageImpl<>(new ArrayList<>(items));
      return new ResponseEntity<>(singleResult, HttpStatus.OK);
    } else {
      Page<Item> items = itemService.findByMerchant(merchant, pageable);
      return new ResponseEntity<>(items, HttpStatus.OK);
    }
  }

  /**
   * Get Item with given item id.
   *
   * @param itemId
   * @return Item object or 404 if item is not found
   */
  @RequestMapping(
      value = "/{itemId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Item> getItemById(@PathVariable("itemId") Long itemId) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Item item = itemService.findById(itemId);
    if (item == null || !item.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(item, HttpStatus.OK);
  }

  /**
   * Create new item.
   *
   * @param item
   * @return Item object (created item object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Item> createItem(@RequestBody Item item) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    item.setMerchant(merchant);

    item = itemService.create(item);
    if (item == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(item, HttpStatus.CREATED);
  }

  /**
   * Update item.
   *
   * @param item
   * @return Item object (updated item object)
   */
  @RequestMapping(
      value = "/{itemId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Item> updateItem(@RequestBody Item item) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Item existingItem = itemService.findById(item.getId());
    if (existingItem == null || !existingItem.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    item.setMerchant(merchant);
    item = itemService.update(item);
    if (item == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(item, HttpStatus.OK);
  }
}
