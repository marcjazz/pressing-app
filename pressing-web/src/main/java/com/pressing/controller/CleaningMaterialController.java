package com.pressing.controller;

import com.pressing.mapper.EntityMapper;
import com.pressing.model.CleaningMaterial;
import com.pressing.model.CleaningMaterialDTO;
import com.pressing.model.Merchant;
import com.pressing.service.CleaningMaterialService;
import com.pressing.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
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
@RequestMapping("api/v1/materials")
public class CleaningMaterialController {

  @Autowired private CleaningMaterialService cleaningMaterialService;
  @Autowired private UserService userService;
  @Autowired private EntityMapper entityMapper;

  /**
   * Get all cleaning materials or cleaning material with a given name.
   *
   * @param materialName
   * @return Collection of cleaning materials or material with the given name
   */
  @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<CleaningMaterialDTO>> getCleaningMaterials(
      @RequestParam(value = "materialName", required = false) String materialName,
      Pageable pageable) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    if (materialName != null) {
      Collection<CleaningMaterialDTO> materials = new ArrayList<>();
      CleaningMaterial material = cleaningMaterialService.findByName(materialName);
      if (material != null && material.getMerchant().equals(merchant)) {
        materials.add(entityMapper.toDTO(material));
      }
      Page<CleaningMaterialDTO> singleResult = new PageImpl<>(new ArrayList<>(materials));
      return new ResponseEntity<>(singleResult, HttpStatus.OK);
    } else {
      Page<CleaningMaterial> materials = cleaningMaterialService.findByMerchant(merchant, pageable);
      List<CleaningMaterialDTO> materialDTOs =
          materials.getContent().stream().map(entityMapper::toDTO).collect(Collectors.toList());
      Page<CleaningMaterialDTO> result =
          new PageImpl<>(materialDTOs, pageable, materials.getTotalElements());
      return new ResponseEntity<>(result, HttpStatus.OK);
    }
  }

  /**
   * Get cleaning material with given material id.
   *
   * @param materialId
   * @return CleaningMaterial object or 404 if material is not found
   */
  @RequestMapping(
      value = "/{materialId}",
      method = RequestMethod.GET,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CleaningMaterialDTO> getCleaningMaterialById(
      @PathVariable("materialId") Long materialId) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    CleaningMaterial material = cleaningMaterialService.findById(materialId);
    if (material == null || !material.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(entityMapper.toDTO(material), HttpStatus.OK);
  }

  /**
   * Create new cleaning material.
   *
   * @param materialDTO
   * @return CleaningMaterial object (created object)
   */
  @RequestMapping(
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CleaningMaterialDTO> createCleaningMaterial(
      @RequestBody CleaningMaterialDTO materialDTO) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
    CleaningMaterial material = entityMapper.toEntity(materialDTO);
    material.setMerchant(merchant);

    material = cleaningMaterialService.create(material);
    if (material == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(material), HttpStatus.CREATED);
  }

  /**
   * Update cleaning material.
   *
   * @param materialDTO
   * @return CleaningMaterial object (updated object).
   */
  @RequestMapping(
      value = "/{materialId}",
      method = RequestMethod.PUT,
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CleaningMaterialDTO> updateCleaningMaterial(
      @RequestBody CleaningMaterialDTO materialDTO) {

    Merchant merchant = userService.getCurrentUser().getMerchant();
    if (merchant == null) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    CleaningMaterial existingMaterial = cleaningMaterialService.findById(materialDTO.getId());
    if (existingMaterial == null || !existingMaterial.getMerchant().equals(merchant)) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    CleaningMaterial material = entityMapper.toEntity(materialDTO);
    material.setMerchant(merchant);
    material = cleaningMaterialService.update(material);
    if (material == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(entityMapper.toDTO(material), HttpStatus.OK);
  }
}
