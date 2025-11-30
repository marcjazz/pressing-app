package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.Permission;
import com.pressing.model.PermissionDTO;
import com.pressing.service.PermissionService;
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
@RequestMapping("api/v1/permissions")
public class PermissionController {

  @Autowired private PermissionService permissionService;
  @Autowired private EntityMapper entityMapper;

  /**
   * Get all permissions or permission with a given name.
   *
   * @param permissionName
   * @return Collection of permissions or permission with the given name
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<PermissionDTO>> getPermissions(
      @RequestParam(value = "permissionName", required = false) String permissionName,
      Pageable pageable) {

    Page<Permission> permissions;
    if (permissionName != null) {
      Permission permission = permissionService.findByName(permissionName);
      if (permission != null) {
        permissions = new PageImpl<>(Collections.singletonList(permission));
      } else {
        permissions = Page.empty();
      }
    } else {
      permissions = permissionService.findAll(pageable);
    }

    List<PermissionDTO> dtos =
        permissions.getContent().stream().map(entityMapper::toDTO).collect(Collectors.toList());
    Page<PermissionDTO> result = new PageImpl<>(dtos, pageable, permissions.getTotalElements());

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  /**
   * Get permission with a given id.
   *
   * @param permissionId
   * @return Permission object or 404 if permission is not found
   */
  @RequestMapping(
      value = "/{permissionId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PermissionDTO> getPermissionById(
      @PathVariable("permissionId") Long permissionId) {

    Permission permission = permissionService.findById(permissionId);
    if (permission == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(entityMapper.toDTO(permission), HttpStatus.OK);
  }

  /**
   * Create new permission.
   *
   * @param permissionDTO
   * @return Permission object (created object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {

    Permission newPermission = entityMapper.toEntity(permissionDTO);
    newPermission = permissionService.create(newPermission);
    if (newPermission == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(newPermission), HttpStatus.CREATED);
  }

  /**
   * Update permission record.
   *
   * @param permissionDTO
   * @return Permission object (updated object)
   */
  @RequestMapping(
      value = "/{permissionId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PermissionDTO> updatePermission(@RequestBody PermissionDTO permissionDTO) {

    Permission permission = entityMapper.toEntity(permissionDTO);
    permission = permissionService.update(permission);
    if (permission == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(permission), HttpStatus.OK);
  }
}
