package com.pressing.controller;

import com.pressing.model.CustomUser;
import com.pressing.model.Merchant;
import com.pressing.model.Role;
import com.pressing.model.UserDTO;
import com.pressing.service.RoleService;
import com.pressing.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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
@RequestMapping("api/v1/users")
public class UserController {

  @Autowired private UserService userService;

  @Autowired private RoleService roleService;

  /**
   * Get all uses or user with a given username.
   *
   * @param username, isActive
   * @return Collection of users or user with the given username
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<CustomUser>> getUsers(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "active", required = false) String isActive,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sort) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by(sort));

    if (username != null) {
      // Username search is unique, so we can just return a page with one item or empty
      CustomUser user = userService.findByUserName(username);
      if (user != null && user.getMerchant().equals(merchant)) {
        // Create a PageImpl manually if needed, but for now let's stick to the pattern
        // Since findByUserName returns a single object, we might need to adjust logic or return type if we want strict pagination on this specific query
        // However, usually username search is exact match. Let's wrap it.
        // For simplicity in this refactor, if username is present, we return a list wrapped in a page implementation or similar.
        // But wait, the return type is Page<CustomUser>. 
        // Let's use a helper or just return a PageImpl.
        // Since PageImpl is in spring-data-commons, we can use it.
        return new ResponseEntity<>(new org.springframework.data.domain.PageImpl<>(Collections.singletonList(user)), HttpStatus.OK);
      }
      return new ResponseEntity<>(org.springframework.data.domain.Page.empty(), HttpStatus.OK);
    } else if (isActive != null) {
      boolean active = Boolean.parseBoolean(isActive);
      return new ResponseEntity<>(userService.findByMerchantAndIsActive(merchant, active, pageable), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(userService.findByMerchant(merchant, pageable), HttpStatus.OK);
    }
  }

  /**
   * Get user with given user id.
   *
   * @param userId
   * @return User object or 404 if user is not found
   */
  @RequestMapping(
      value = "/{id}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomUser> getUserById(@NonNull @PathVariable("id") Long userId) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    CustomUser user = userService.findById(userId);
    if (user == null || !user.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Create new user.
   *
   * @param user
   * @return User object (created user object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomUser> createUser(@RequestBody UserDTO user) {
    int count = 0;
    List<Role> roles = new ArrayList<>();
    while (user.getRoleIds().size() > count) {
      roles.add(roleService.findById(user.getRoleIds().get(count++)));
    }
    CustomUser newUser = new CustomUser();
    newUser.setFirstName(user.getFirstName());
    newUser.setLastName(user.getLastName());
    newUser.setUsername(user.getUsername());
    newUser.setPassword(user.getPassword());
    newUser.setActive(user.isActive());
    newUser.setRoles(roles);
    newUser.setTelephone(user.getTelephone());

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant != null) {
      newUser.setMerchant(merchant);
    }

    Optional<CustomUser> createdUser = userService.create(newUser);
    if (!createdUser.isPresent()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    newUser = createdUser.get();

    return new ResponseEntity<>(newUser, HttpStatus.CREATED);
  }

  /**
   * Update user's information.
   *
   * @param userId
   * @return User object (updated user object)
   */
  @RequestMapping(
      value = "/{userId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CustomUser> updateUser(
      @NonNull @PathVariable("userId") Long userId, @RequestBody UserDTO user) {
    int count = 0;
    List<Role> roles = new ArrayList<>();
    while (user.getRoleIds().size() > count) {
      roles.add(roleService.findById(user.getRoleIds().get(count++)));
    }

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    CustomUser updateUser = userService.findById(userId);
    if (updateUser == null || !updateUser.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    updateUser.setFirstName(user.getFirstName());
    updateUser.setLastName(user.getLastName());
    updateUser.setPassword(user.getPassword());
    updateUser.setTelephone(user.getTelephone());
    updateUser.setActive(user.isActive());
    updateUser.setRoles(roles);
    updateUser = userService.update(updateUser);

    if (updateUser == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(updateUser, HttpStatus.OK);
  }

  /**
   * Delete user's information
   *
   * @param userId
   * @return HTTP status 204, .
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<CustomUser> deactivateUser(@NonNull @PathVariable("userId") Long userId) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    CustomUser user = userService.findById(userId);
    if (user == null || !user.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    userService.deactivate(userId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
