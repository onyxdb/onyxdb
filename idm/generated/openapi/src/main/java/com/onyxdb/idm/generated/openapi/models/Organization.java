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
 * Organization
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class Organization {

  private UUID id;

  private String name;

  private String description;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  private UUID resourceId;

  private UUID accountId;

  private UUID ownerId;

  private UUID responsibleId;

  @Valid
  private List<String> participants = new ArrayList<>();

  public Organization id(UUID id) {
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

  public Organization name(String name) {
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

  public Organization description(String description) {
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

  public Organization createdAt(LocalDateTime createdAt) {
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

  public Organization updatedAt(LocalDateTime updatedAt) {
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

  public Organization resourceId(UUID resourceId) {
    this.resourceId = resourceId;
    return this;
  }

  /**
   * Get resourceId
   * @return resourceId
  */
  @Valid 
  @Schema(name = "resourceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resourceId")
  public UUID getResourceId() {
    return resourceId;
  }

  public void setResourceId(UUID resourceId) {
    this.resourceId = resourceId;
  }

  public Organization accountId(UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
  */
  @Valid 
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountId")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public Organization ownerId(UUID ownerId) {
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

  public Organization responsibleId(UUID responsibleId) {
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

  public Organization participants(List<String> participants) {
    this.participants = participants;
    return this;
  }

  public Organization addParticipantsItem(String participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
  */
  
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Organization organization = (Organization) o;
    return Objects.equals(this.id, organization.id) &&
        Objects.equals(this.name, organization.name) &&
        Objects.equals(this.description, organization.description) &&
        Objects.equals(this.createdAt, organization.createdAt) &&
        Objects.equals(this.updatedAt, organization.updatedAt) &&
        Objects.equals(this.resourceId, organization.resourceId) &&
        Objects.equals(this.accountId, organization.accountId) &&
        Objects.equals(this.ownerId, organization.ownerId) &&
        Objects.equals(this.responsibleId, organization.responsibleId) &&
        Objects.equals(this.participants, organization.participants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, createdAt, updatedAt, resourceId, accountId, ownerId, responsibleId, participants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Organization {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    resourceId: ").append(toIndentedString(resourceId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    responsibleId: ").append(toIndentedString(responsibleId)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
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

