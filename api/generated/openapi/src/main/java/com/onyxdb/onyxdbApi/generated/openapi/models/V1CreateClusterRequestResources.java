package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestResourcesStorage;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * V1CreateClusterRequestResources
 */

@JsonTypeName("V1CreateClusterRequest_resources")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class V1CreateClusterRequestResources {

  private V1CreateClusterRequestResourcesStorage storage;

  public V1CreateClusterRequestResources() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public V1CreateClusterRequestResources(V1CreateClusterRequestResourcesStorage storage) {
    this.storage = storage;
  }

  public V1CreateClusterRequestResources storage(V1CreateClusterRequestResourcesStorage storage) {
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
  public V1CreateClusterRequestResourcesStorage getStorage() {
    return storage;
  }

  public void setStorage(V1CreateClusterRequestResourcesStorage storage) {
    this.storage = storage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    V1CreateClusterRequestResources v1CreateClusterRequestResources = (V1CreateClusterRequestResources) o;
    return Objects.equals(this.storage, v1CreateClusterRequestResources.storage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(storage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1CreateClusterRequestResources {\n");
    sb.append("    storage: ").append(toIndentedString(storage)).append("\n");
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

