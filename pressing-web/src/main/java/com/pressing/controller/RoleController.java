package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.Permission;
import com.pressing.model.PermissionDTO;
import com.pressing.model.Role;
import com.pressing.model.RoleDTO;
import com.pressing.service.PermissionService;
import com.pressing.service.RoleService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("api/v1/roles")
public class RoleController {

  @Autowired private RoleService roleService;

  @Autowired private PermissionService permissionService;

  @Autowired private EntityMapper entityMapper;

  /**
   * Get all roles or role with a given name.
   *
   * @param roleName
   * @return Collection of roles in the system or role with the given name
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<RoleDTO>> getRoles(
      @RequestParam(value = "roleName", required = false) String roleName, Pageable pageable) {

    Page<Role> roles;
    if (roleName != null) {
      Role role = roleService.findByName(roleName);
      if (role != null) {
        roles = new PageImpl<>(Collections.singletonList(role));
      } else {
        roles = Page.empty();
      }
    } else {
      roles = roleService.findAll(pageable);
    }

    List<RoleDTO> dtos =
        roles.getContent().stream().map(entityMapper::toDTO).collect(Collectors.toList());
    Page<RoleDTO> result = new PageImpl<>(dtos, pageable, roles.getTotalElements());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * Get role with a given id.
   *
   * @param roleId
   * @return Role object or 404 if role is not found
   */
  @RequestMapping(
      value = "/{roleId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RoleDTO> getPaymentMethodById(@PathVariable("roleId") Long roleId) {

    Role role = roleService.findById(roleId);
    if (role == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(entityMapper.toDTO(role), HttpStatus.OK);
  }

  /**
   * Get all permissions in role.
   *
   * @param roleId
   * @return Collection of permissions
   */
  @RequestMapping(
      value = "/{roleId}/permissions",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Collection<PermissionDTO>> getRolePermissions(
      @PathVariable("roleId") Long roleId) {

    Collection<Permission> permissions = roleService.findById(roleId).getPermissions();
    if (permissions == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    List<PermissionDTO> permissionDTOs =
        permissions.stream().map(entityMapper::toDTO).collect(Collectors.toList());

    return new ResponseEntity<>(permissionDTOs, HttpStatus.OK);
  }

  /**
   * Create new role.
   *
   * @param roleDTO
   * @return Role Object (created role object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
    int count = 0;
    List<Permission> permissions = new ArrayList<>();
    while (roleDTO.getPermissionIds().size() > count) {
      permissions.add(permissionService.findById(roleDTO.getPermissionIds().get(count++)));
    }
    Role newRole = new Role();
    newRole.setName(roleDTO.getName());
    newRole.setDescription(roleDTO.getDescription());
    newRole.setPermissions(permissions);
    newRole = roleService.create(newRole);
    if (newRole == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(newRole), HttpStatus.CREATED);
  }

  /**
   * Update role record.
   *
   * @param roleDTO
   * @return Role object (updated role object).
   */
  @RequestMapping(
      value = "/{roleId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<RoleDTO> updateRole(@RequestBody RoleDTO roleDTO) {
    int count = 0;
    List<Permission> permissions = new ArrayList<>();
    while (roleDTO.getPermissionIds().size() > count) {
      permissions.add(permissionService.findById(roleDTO.getPermissionIds().get(count++)));
    }
    Role newRole = roleService.findById(roleDTO.getId());
    newRole.setDescription(roleDTO.getDescription());
    newRole.setPermissions(permissions);
    newRole = roleService.create(newRole);
    newRole = roleService.update(newRole);

    if (newRole == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(newRole), HttpStatus.OK);
  }
}
