package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateClusterRequestStorage
 */

@JsonTypeName("CreateClusterRequest_storage")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class CreateClusterRequestStorage {

  private Long diskSize;

  public CreateClusterRequestStorage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateClusterRequestStorage(Long diskSize) {
    this.diskSize = diskSize;
  }

  public CreateClusterRequestStorage diskSize(Long diskSize) {
    this.diskSize = diskSize;
    return this;
  }

  /**
   * Disk size in bytes.
   * @return diskSize
  */
  @NotNull 
  @Schema(name = "diskSize", description = "Disk size in bytes.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("diskSize")
  public Long getDiskSize() {
    return diskSize;
  }

  public void setDiskSize(Long diskSize) {
    this.diskSize = diskSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateClusterRequestStorage createClusterRequestStorage = (CreateClusterRequestStorage) o;
    return Objects.equals(this.diskSize, createClusterRequestStorage.diskSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(diskSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateClusterRequestStorage {\n");
    sb.append("    diskSize: ").append(toIndentedString(diskSize)).append("\n");
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

