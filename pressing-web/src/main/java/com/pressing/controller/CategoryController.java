package com.pressing.controller;

import com.pressing.model.Category;
import com.pressing.model.Item;
import com.pressing.service.CategoryService;
import com.pressing.service.ItemService;
import java.util.ArrayList;
import java.util.Collection;
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
@RequestMapping("api/v1/categories")
public class CategoryController {

  @Autowired private CategoryService categoryService;

  @Autowired private ItemService itemService;

  @Autowired private com.pressing.service.UserService userService;

  /**
   * Get all categories or category with a given name.
   *
   * @param categoryName
   * @return collection of categories or category with the given name
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Category>> getCategories(
      @RequestParam(value = "categoryName", required = false) String categoryName, Pageable pageable) {

    if (categoryName != null) {
      // This part is not paginated, as it returns a single or no category.
      // For a more consistent API, you might consider returning a Page with a single element.
      Collection<Category> categories = new ArrayList<>();
      Category category = categoryService.findByName(categoryName);
      if (category != null) {
        categories.add(category);
      }
      // Returning a Page for a single item search might be complex, so we can return a list for this specific case.
      // Or, for consistency, create a Page object from the list.
      Page<Category> singleResult = new PageImpl<>(new ArrayList<>(categories));
      return new ResponseEntity<>(singleResult, HttpStatus.OK);
    } else {
      Page<Category> allCategories = categoryService.findAll(pageable);
      return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }
  }

  /**
   * Get category with given category id.
   *
   * @param categoryId
   * @return Category object or 404 if category is not found
   */
  @RequestMapping(
      value = "/{categoryId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {

    Category category = categoryService.findById(categoryId);
    if (category == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(category, HttpStatus.OK);
  }

  /**
   * Get items with same category.
   *
   * @param categoryId
   * @return collection of items
   */
  @RequestMapping(
      value = "/{categoryId}/items",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<Item>> getCategoryItems(
      @PathVariable("categoryId") Long categoryId) {

    Collection<Item> items = itemService.findCategoryItems(categoryId);
    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  /**
   * Create new category.
   *
   * @param category
   * @return Category Object (created object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Category> createCategory(@RequestBody Category category) {
    category.setMerchant(userService.getCurrentUser().getMerchant());
    category = categoryService.create(category);
    if (category == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(category, HttpStatus.CREATED);
  }

  /**
   * Update category.
   *
   * @param category
   * @return Category Object (updated object).
   */
  @RequestMapping(
      value = "/{categoryId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Category> updateCategory(@RequestBody Category category) {

    category = categoryService.update(category);
    if (category == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(category, HttpStatus.OK);
  }
}
