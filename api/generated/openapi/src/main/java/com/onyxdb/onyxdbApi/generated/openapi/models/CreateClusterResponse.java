package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * CreateClusterResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class CreateClusterResponse {

  private UUID clusterId;

  public CreateClusterResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateClusterResponse(UUID clusterId) {
    this.clusterId = clusterId;
  }

  public CreateClusterResponse clusterId(UUID clusterId) {
    this.clusterId = clusterId;
    return this;
  }

  /**
   * Get clusterId
   * @return clusterId
  */
  @NotNull @Valid 
  @Schema(name = "clusterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("clusterId")
  public UUID getClusterId() {
    return clusterId;
  }

  public void setClusterId(UUID clusterId) {
    this.clusterId = clusterId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateClusterResponse createClusterResponse = (CreateClusterResponse) o;
    return Objects.equals(this.clusterId, createClusterResponse.clusterId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clusterId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateClusterResponse {\n");
    sb.append("    clusterId: ").append(toIndentedString(clusterId)).append("\n");
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

