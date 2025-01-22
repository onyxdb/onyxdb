package com.onyxdb.idm.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateGroupRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class UpdateGroupRequest {

  private String name;

  @Valid
  private List<UUID> accountIds = new ArrayList<>();

  @Valid
  private List<UUID> groupIds = new ArrayList<>();

  @Valid
  private List<UUID> roleIds = new ArrayList<>();

  public UpdateGroupRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateGroupRequest(String name) {
    this.name = name;
  }

  public UpdateGroupRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UpdateGroupRequest accountIds(List<UUID> accountIds) {
    this.accountIds = accountIds;
    return this;
  }

  public UpdateGroupRequest addAccountIdsItem(UUID accountIdsItem) {
    if (this.accountIds == null) {
      this.accountIds = new ArrayList<>();
    }
    this.accountIds.add(accountIdsItem);
    return this;
  }

  /**
   * Get accountIds
   * @return accountIds
  */
  @Valid 
  @Schema(name = "accountIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountIds")
  public List<UUID> getAccountIds() {
    return accountIds;
  }

  public void setAccountIds(List<UUID> accountIds) {
    this.accountIds = accountIds;
  }

  public UpdateGroupRequest groupIds(List<UUID> groupIds) {
    this.groupIds = groupIds;
    return this;
  }

  public UpdateGroupRequest addGroupIdsItem(UUID groupIdsItem) {
    if (this.groupIds == null) {
      this.groupIds = new ArrayList<>();
    }
    this.groupIds.add(groupIdsItem);
    return this;
  }

  /**
   * Get groupIds
   * @return groupIds
  */
  @Valid 
  @Schema(name = "groupIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("groupIds")
  public List<UUID> getGroupIds() {
    return groupIds;
  }

  public void setGroupIds(List<UUID> groupIds) {
    this.groupIds = groupIds;
  }

  public UpdateGroupRequest roleIds(List<UUID> roleIds) {
    this.roleIds = roleIds;
    return this;
  }

  public UpdateGroupRequest addRoleIdsItem(UUID roleIdsItem) {
    if (this.roleIds == null) {
      this.roleIds = new ArrayList<>();
    }
    this.roleIds.add(roleIdsItem);
    return this;
  }

  /**
   * Get roleIds
   * @return roleIds
  */
  @Valid 
  @Schema(name = "roleIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roleIds")
  public List<UUID> getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(List<UUID> roleIds) {
    this.roleIds = roleIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateGroupRequest updateGroupRequest = (UpdateGroupRequest) o;
    return Objects.equals(this.name, updateGroupRequest.name) &&
        Objects.equals(this.accountIds, updateGroupRequest.accountIds) &&
        Objects.equals(this.groupIds, updateGroupRequest.groupIds) &&
        Objects.equals(this.roleIds, updateGroupRequest.roleIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, accountIds, groupIds, roleIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateGroupRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    accountIds: ").append(toIndentedString(accountIds)).append("\n");
    sb.append("    groupIds: ").append(toIndentedString(groupIds)).append("\n");
    sb.append("    roleIds: ").append(toIndentedString(roleIds)).append("\n");
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

