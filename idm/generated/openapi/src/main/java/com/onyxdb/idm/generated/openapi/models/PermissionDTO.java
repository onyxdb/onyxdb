package com.onyxdb.idm.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PermissionDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class PermissionDTO {

  private UUID id;

  private String actionType;

  private String resourceType;

  @Valid
  private List<String> resourceFields = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  public PermissionDTO id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public PermissionDTO actionType(String actionType) {
    this.actionType = actionType;
    return this;
  }

  /**
   * Get actionType
   * @return actionType
  */
  
  @Schema(name = "actionType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionType")
  public String getActionType() {
    return actionType;
  }

  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  public PermissionDTO resourceType(String resourceType) {
    this.resourceType = resourceType;
    return this;
  }

  /**
   * Get resourceType
   * @return resourceType
  */
  
  @Schema(name = "resourceType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resourceType")
  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public PermissionDTO resourceFields(List<String> resourceFields) {
    this.resourceFields = resourceFields;
    return this;
  }

  public PermissionDTO addResourceFieldsItem(String resourceFieldsItem) {
    if (this.resourceFields == null) {
      this.resourceFields = new ArrayList<>();
    }
    this.resourceFields.add(resourceFieldsItem);
    return this;
  }

  /**
   * Get resourceFields
   * @return resourceFields
  */
  
  @Schema(name = "resourceFields", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resourceFields")
  public List<String> getResourceFields() {
    return resourceFields;
  }

  public void setResourceFields(List<String> resourceFields) {
    this.resourceFields = resourceFields;
  }

  public PermissionDTO createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PermissionDTO updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
  */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PermissionDTO permissionDTO = (PermissionDTO) o;
    return Objects.equals(this.id, permissionDTO.id) &&
        Objects.equals(this.actionType, permissionDTO.actionType) &&
        Objects.equals(this.resourceType, permissionDTO.resourceType) &&
        Objects.equals(this.resourceFields, permissionDTO.resourceFields) &&
        Objects.equals(this.createdAt, permissionDTO.createdAt) &&
        Objects.equals(this.updatedAt, permissionDTO.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, actionType, resourceType, resourceFields, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PermissionDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    actionType: ").append(toIndentedString(actionType)).append("\n");
    sb.append("    resourceType: ").append(toIndentedString(resourceType)).append("\n");
    sb.append("    resourceFields: ").append(toIndentedString(resourceFields)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

