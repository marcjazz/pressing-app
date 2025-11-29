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
import java.util.stream.Collectors;
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
  public ResponseEntity<Collection<CustomUser>> getUsers(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "active", required = false) String isActive) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Collection<CustomUser> users = new ArrayList<>();
    if (username != null) {
      CustomUser user = userService.findByUserName(username);
      if (user != null && user.getMerchant().equals(merchant)) {
        users.add(user);
      }
    } else if (isActive != null) {
      if (isActive.compareToIgnoreCase("true") == 0) {
        Collection<CustomUser> activeUsers = userService.findByIsActive(true);
        users.addAll(
            activeUsers.stream()
                .filter(user -> user.getMerchant().equals(merchant))
                .collect(Collectors.toList()));

      } else if (isActive.compareToIgnoreCase("false") == 0) {
        Collection<CustomUser> deActivatedUsers = userService.findByIsActive(false);
        users.addAll(
            deActivatedUsers.stream()
                .filter(user -> user.getMerchant().equals(merchant))
                .collect(Collectors.toList()));
      }

    } else {
      Collection<CustomUser> allUser = userService.findAll();
      users.addAll(
          allUser.stream()
              .filter(user -> user.getMerchant().equals(merchant))
              .collect(Collectors.toList()));
    }

    return new ResponseEntity<>(users, HttpStatus.OK);
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
