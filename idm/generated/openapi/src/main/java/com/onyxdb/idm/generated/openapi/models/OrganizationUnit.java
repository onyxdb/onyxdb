package com.onyxdb.idm.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OrganizationUnit
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class OrganizationUnit {

  private UUID id;

  private String name;

  private String description;

  private UUID domainComponentId;

  private UUID parentId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  private UUID ownerId;

  private UUID responsibleId;

  public OrganizationUnit id(UUID id) {
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

  public OrganizationUnit name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OrganizationUnit description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public OrganizationUnit domainComponentId(UUID domainComponentId) {
    this.domainComponentId = domainComponentId;
    return this;
  }

  /**
   * Get domainComponentId
   * @return domainComponentId
  */
  @Valid 
  @Schema(name = "domainComponentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("domainComponentId")
  public UUID getDomainComponentId() {
    return domainComponentId;
  }

  public void setDomainComponentId(UUID domainComponentId) {
    this.domainComponentId = domainComponentId;
  }

  public OrganizationUnit parentId(UUID parentId) {
    this.parentId = parentId;
    return this;
  }

  /**
   * Get parentId
   * @return parentId
  */
  @Valid 
  @Schema(name = "parentId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("parentId")
  public UUID getParentId() {
    return parentId;
  }

  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  public OrganizationUnit createdAt(LocalDateTime createdAt) {
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

  public OrganizationUnit updatedAt(LocalDateTime updatedAt) {
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

  public OrganizationUnit ownerId(UUID ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Get ownerId
   * @return ownerId
  */
  @Valid 
  @Schema(name = "ownerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ownerId")
  public UUID getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(UUID ownerId) {
    this.ownerId = ownerId;
  }

  public OrganizationUnit responsibleId(UUID responsibleId) {
    this.responsibleId = responsibleId;
    return this;
  }

  /**
   * Get responsibleId
   * @return responsibleId
  */
  @Valid 
  @Schema(name = "responsibleId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("responsibleId")
  public UUID getResponsibleId() {
    return responsibleId;
  }

  public void setResponsibleId(UUID responsibleId) {
    this.responsibleId = responsibleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrganizationUnit organizationUnit = (OrganizationUnit) o;
    return Objects.equals(this.id, organizationUnit.id) &&
        Objects.equals(this.name, organizationUnit.name) &&
        Objects.equals(this.description, organizationUnit.description) &&
        Objects.equals(this.domainComponentId, organizationUnit.domainComponentId) &&
        Objects.equals(this.parentId, organizationUnit.parentId) &&
        Objects.equals(this.createdAt, organizationUnit.createdAt) &&
        Objects.equals(this.updatedAt, organizationUnit.updatedAt) &&
        Objects.equals(this.ownerId, organizationUnit.ownerId) &&
        Objects.equals(this.responsibleId, organizationUnit.responsibleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, domainComponentId, parentId, createdAt, updatedAt, ownerId, responsibleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrganizationUnit {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    domainComponentId: ").append(toIndentedString(domainComponentId)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    responsibleId: ").append(toIndentedString(responsibleId)).append("\n");
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

