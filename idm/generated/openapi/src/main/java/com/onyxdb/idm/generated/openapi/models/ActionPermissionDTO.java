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
 * ActionPermissionDTO
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class ActionPermissionDTO {

  private UUID id;

  private String actionType;

  @Valid
  private List<String> resourceFields = new ArrayList<>();

  @Valid
  private List<String> labels = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  public ActionPermissionDTO id(UUID id) {
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

  public ActionPermissionDTO actionType(String actionType) {
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

  public ActionPermissionDTO resourceFields(List<String> resourceFields) {
    this.resourceFields = resourceFields;
    return this;
  }

  public ActionPermissionDTO addResourceFieldsItem(String resourceFieldsItem) {
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

  public ActionPermissionDTO labels(List<String> labels) {
    this.labels = labels;
    return this;
  }

  public ActionPermissionDTO addLabelsItem(String labelsItem) {
    if (this.labels == null) {
      this.labels = new ArrayList<>();
    }
    this.labels.add(labelsItem);
    return this;
  }

  /**
   * Get labels
   * @return labels
  */
  
  @Schema(name = "labels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("labels")
  public List<String> getLabels() {
    return labels;
  }

  public void setLabels(List<String> labels) {
    this.labels = labels;
  }

  public ActionPermissionDTO createdAt(LocalDateTime createdAt) {
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

  public ActionPermissionDTO updatedAt(LocalDateTime updatedAt) {
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
    ActionPermissionDTO actionPermissionDTO = (ActionPermissionDTO) o;
    return Objects.equals(this.id, actionPermissionDTO.id) &&
        Objects.equals(this.actionType, actionPermissionDTO.actionType) &&
        Objects.equals(this.resourceFields, actionPermissionDTO.resourceFields) &&
        Objects.equals(this.labels, actionPermissionDTO.labels) &&
        Objects.equals(this.createdAt, actionPermissionDTO.createdAt) &&
        Objects.equals(this.updatedAt, actionPermissionDTO.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, actionType, resourceFields, labels, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionPermissionDTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    actionType: ").append(toIndentedString(actionType)).append("\n");
    sb.append("    resourceFields: ").append(toIndentedString(resourceFields)).append("\n");
    sb.append("    labels: ").append(toIndentedString(labels)).append("\n");
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

