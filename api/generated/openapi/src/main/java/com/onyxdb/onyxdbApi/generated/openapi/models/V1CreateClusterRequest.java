package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestResources;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestSpec;
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

  private V1CreateClusterRequestResources resources;

  private V1CreateClusterRequestSpec spec;

  public V1CreateClusterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public V1CreateClusterRequest(String name, V1CreateClusterRequestResources resources, V1CreateClusterRequestSpec spec) {
    this.name = name;
    this.resources = resources;
    this.spec = spec;
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
  @Schema(name = "name", example = "Cluster name", requiredMode = Schema.RequiredMode.REQUIRED)
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
  @Schema(name = "description", example = "Cluster description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public V1CreateClusterRequest resources(V1CreateClusterRequestResources resources) {
    this.resources = resources;
    return this;
  }

  /**
   * Get resources
   * @return resources
  */
  @NotNull @Valid 
  @Schema(name = "resources", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("resources")
  public V1CreateClusterRequestResources getResources() {
    return resources;
  }

  public void setResources(V1CreateClusterRequestResources resources) {
    this.resources = resources;
  }

  public V1CreateClusterRequest spec(V1CreateClusterRequestSpec spec) {
    this.spec = spec;
    return this;
  }

  /**
   * Get spec
   * @return spec
  */
  @NotNull @Valid 
  @Schema(name = "spec", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("spec")
  public V1CreateClusterRequestSpec getSpec() {
    return spec;
  }

  public void setSpec(V1CreateClusterRequestSpec spec) {
    this.spec = spec;
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
        Objects.equals(this.resources, v1CreateClusterRequest.resources) &&
        Objects.equals(this.spec, v1CreateClusterRequest.spec);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, resources, spec);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1CreateClusterRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
    sb.append("    spec: ").append(toIndentedString(spec)).append("\n");
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

