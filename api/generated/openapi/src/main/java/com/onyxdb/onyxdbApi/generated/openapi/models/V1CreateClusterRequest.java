package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestDbSpec;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestStorage;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * V1CreateClusterRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class V1CreateClusterRequest {

  private String name;

  private String description;

  private V1CreateClusterRequestStorage storage;

  private V1CreateClusterRequestDbSpec dbSpec;

  public V1CreateClusterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public V1CreateClusterRequest(String name, V1CreateClusterRequestStorage storage, V1CreateClusterRequestDbSpec dbSpec) {
    this.name = name;
    this.storage = storage;
    this.dbSpec = dbSpec;
  }

  public V1CreateClusterRequest name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  @NotNull @Size(min = 1, max = 64) 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public V1CreateClusterRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  @Size(min = 1, max = 256) 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public V1CreateClusterRequest storage(V1CreateClusterRequestStorage storage) {
    this.storage = storage;
    return this;
  }

  /**
   * Get storage
   * @return storage
  */
  @NotNull @Valid 
  @Schema(name = "storage", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("storage")
  public V1CreateClusterRequestStorage getStorage() {
    return storage;
  }

  public void setStorage(V1CreateClusterRequestStorage storage) {
    this.storage = storage;
  }

  public V1CreateClusterRequest dbSpec(V1CreateClusterRequestDbSpec dbSpec) {
    this.dbSpec = dbSpec;
    return this;
  }

  /**
   * Get dbSpec
   * @return dbSpec
  */
  @NotNull @Valid 
  @Schema(name = "dbSpec", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dbSpec")
  public V1CreateClusterRequestDbSpec getDbSpec() {
    return dbSpec;
  }

  public void setDbSpec(V1CreateClusterRequestDbSpec dbSpec) {
    this.dbSpec = dbSpec;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    V1CreateClusterRequest v1CreateClusterRequest = (V1CreateClusterRequest) o;
    return Objects.equals(this.name, v1CreateClusterRequest.name) &&
        Objects.equals(this.description, v1CreateClusterRequest.description) &&
        Objects.equals(this.storage, v1CreateClusterRequest.storage) &&
        Objects.equals(this.dbSpec, v1CreateClusterRequest.dbSpec);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, storage, dbSpec);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1CreateClusterRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    storage: ").append(toIndentedString(storage)).append("\n");
    sb.append("    dbSpec: ").append(toIndentedString(dbSpec)).append("\n");
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

