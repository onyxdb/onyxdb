package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterRequestDb;
import com.onyxdb.onyxdbApi.generated.openapi.models.CreateClusterRequestStorage;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateClusterRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class CreateClusterRequest {

  private String name;

  private String description;

  private CreateClusterRequestStorage storage;

  private CreateClusterRequestDb db;

  public CreateClusterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateClusterRequest(String name, CreateClusterRequestStorage storage, CreateClusterRequestDb db) {
    this.name = name;
    this.storage = storage;
    this.db = db;
  }

  public CreateClusterRequest name(String name) {
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

  public CreateClusterRequest description(String description) {
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

  public CreateClusterRequest storage(CreateClusterRequestStorage storage) {
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
  public CreateClusterRequestStorage getStorage() {
    return storage;
  }

  public void setStorage(CreateClusterRequestStorage storage) {
    this.storage = storage;
  }

  public CreateClusterRequest db(CreateClusterRequestDb db) {
    this.db = db;
    return this;
  }

  /**
   * Get db
   * @return db
  */
  @NotNull @Valid 
  @Schema(name = "db", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("db")
  public CreateClusterRequestDb getDb() {
    return db;
  }

  public void setDb(CreateClusterRequestDb db) {
    this.db = db;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateClusterRequest createClusterRequest = (CreateClusterRequest) o;
    return Objects.equals(this.name, createClusterRequest.name) &&
        Objects.equals(this.description, createClusterRequest.description) &&
        Objects.equals(this.storage, createClusterRequest.storage) &&
        Objects.equals(this.db, createClusterRequest.db);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, storage, db);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateClusterRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    storage: ").append(toIndentedString(storage)).append("\n");
    sb.append("    db: ").append(toIndentedString(db)).append("\n");
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

