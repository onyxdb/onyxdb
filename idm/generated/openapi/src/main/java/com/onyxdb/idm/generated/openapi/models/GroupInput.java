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
 * GroupInput
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class GroupInput {

  private String name;

  @Valid
  private List<UUID> accountIds = new ArrayList<>();

  @Valid
  private List<UUID> groupIds = new ArrayList<>();

  @Valid
  private List<UUID> roleIds = new ArrayList<>();

  public GroupInput() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GroupInput(String name) {
    this.name = name;
  }

  public GroupInput name(String name) {
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

  public GroupInput accountIds(List<UUID> accountIds) {
    this.accountIds = accountIds;
    return this;
  }

  public GroupInput addAccountIdsItem(UUID accountIdsItem) {
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

  public GroupInput groupIds(List<UUID> groupIds) {
    this.groupIds = groupIds;
    return this;
  }

  public GroupInput addGroupIdsItem(UUID groupIdsItem) {
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

  public GroupInput roleIds(List<UUID> roleIds) {
    this.roleIds = roleIds;
    return this;
  }

  public GroupInput addRoleIdsItem(UUID roleIdsItem) {
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
    GroupInput groupInput = (GroupInput) o;
    return Objects.equals(this.name, groupInput.name) &&
        Objects.equals(this.accountIds, groupInput.accountIds) &&
        Objects.equals(this.groupIds, groupInput.groupIds) &&
        Objects.equals(this.roleIds, groupInput.roleIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, accountIds, groupIds, roleIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GroupInput {\n");
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

