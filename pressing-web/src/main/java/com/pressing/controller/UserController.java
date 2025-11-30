package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.CustomUser;
import com.pressing.model.Merchant;
import com.pressing.model.Role;
import com.pressing.model.UserDTO;
import com.pressing.service.RoleService;
import com.pressing.service.UserService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  @Autowired private EntityMapper entityMapper;

  /**
   * Get all uses or user with a given username.
   *
   * @param username, isActive
   * @return Collection of users or user with the given username
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<UserDTO>> getUsers(
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
    Page<CustomUser> users;

    if (username != null) {
      CustomUser user = userService.findByUserName(username);
      if (user != null && user.getMerchant().equals(merchant)) {
        users = new PageImpl<>(Collections.singletonList(user));
      } else {
        users = Page.empty();
      }
    } else if (isActive != null) {
      boolean active = Boolean.parseBoolean(isActive);
      users = userService.findByMerchantAndIsActive(merchant, active, pageable);
    } else {
      users = userService.findByMerchant(merchant, pageable);
    }

    List<UserDTO> dtos =
        users.getContent().stream().map(entityMapper::toDTO).collect(Collectors.toList());
    Page<UserDTO> result = new PageImpl<>(dtos, pageable, users.getTotalElements());

    return new ResponseEntity<>(result, HttpStatus.OK);
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
  public ResponseEntity<UserDTO> getUserById(@NonNull @PathVariable("id") Long userId) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    CustomUser user = userService.findById(userId);
    if (user == null || !user.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(entityMapper.toDTO(user), HttpStatus.OK);
  }

  /**
   * Create new user.
   *
   * @param userDTO
   * @return User object (created user object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
    int count = 0;
    List<Role> roles = new ArrayList<>();
    while (userDTO.getRoleIds().size() > count) {
      roles.add(roleService.findById(userDTO.getRoleIds().get(count++)));
    }
    CustomUser newUser = new CustomUser();
    newUser.setFirstName(userDTO.getFirstName());
    newUser.setLastName(userDTO.getLastName());
    newUser.setUsername(userDTO.getUsername());
    newUser.setPassword(userDTO.getPassword());
    newUser.setActive(userDTO.isActive());
    newUser.setRoles(roles);
    newUser.setTelephone(userDTO.getTelephone());

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant != null) {
      newUser.setMerchant(merchant);
    }

    Optional<CustomUser> createdUser = userService.create(newUser);
    if (!createdUser.isPresent()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    newUser = createdUser.get();

    return new ResponseEntity<>(entityMapper.toDTO(newUser), HttpStatus.CREATED);
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
  public ResponseEntity<UserDTO> updateUser(
      @NonNull @PathVariable("userId") Long userId, @RequestBody UserDTO userDTO) {
    int count = 0;
    List<Role> roles = new ArrayList<>();
    while (userDTO.getRoleIds().size() > count) {
      roles.add(roleService.findById(userDTO.getRoleIds().get(count++)));
    }

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    CustomUser updateUser = userService.findById(userId);
    if (updateUser == null || !updateUser.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    updateUser.setFirstName(userDTO.getFirstName());
    updateUser.setLastName(userDTO.getLastName());
    updateUser.setPassword(userDTO.getPassword());
    updateUser.setTelephone(userDTO.getTelephone());
    updateUser.setActive(userDTO.isActive());
    updateUser.setRoles(roles);
    updateUser = userService.update(updateUser);

    if (updateUser == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(entityMapper.toDTO(updateUser), HttpStatus.OK);
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
