package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * V1CreateClusterRequestResourcesStorage
 */

@JsonTypeName("V1CreateClusterRequest_resources_storage")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class V1CreateClusterRequestResourcesStorage {

  private Long diskSizeBytes;

  /**
   * Gets or Sets storageClass
   */
  public enum StorageClassEnum {
    STANDARD("standard");

    private String value;

    StorageClassEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StorageClassEnum fromValue(String value) {
      for (StorageClassEnum b : StorageClassEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StorageClassEnum storageClass;

  public V1CreateClusterRequestResourcesStorage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public V1CreateClusterRequestResourcesStorage(Long diskSizeBytes, StorageClassEnum storageClass) {
    this.diskSizeBytes = diskSizeBytes;
    this.storageClass = storageClass;
  }

  public V1CreateClusterRequestResourcesStorage diskSizeBytes(Long diskSizeBytes) {
    this.diskSizeBytes = diskSizeBytes;
    return this;
  }

  /**
   * Get diskSizeBytes
   * minimum: 1073741824
   * maximum: 10737418240
   * @return diskSizeBytes
  */
  @NotNull @Min(1073741824L) @Max(10737418240L) 
  @Schema(name = "diskSizeBytes", example = "1073741824", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("diskSizeBytes")
  public Long getDiskSizeBytes() {
    return diskSizeBytes;
  }

  public void setDiskSizeBytes(Long diskSizeBytes) {
    this.diskSizeBytes = diskSizeBytes;
  }

  public V1CreateClusterRequestResourcesStorage storageClass(StorageClassEnum storageClass) {
    this.storageClass = storageClass;
    return this;
  }

  /**
   * Get storageClass
   * @return storageClass
  */
  @NotNull 
  @Schema(name = "storageClass", example = "standard", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("storageClass")
  public StorageClassEnum getStorageClass() {
    return storageClass;
  }

  public void setStorageClass(StorageClassEnum storageClass) {
    this.storageClass = storageClass;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    V1CreateClusterRequestResourcesStorage v1CreateClusterRequestResourcesStorage = (V1CreateClusterRequestResourcesStorage) o;
    return Objects.equals(this.diskSizeBytes, v1CreateClusterRequestResourcesStorage.diskSizeBytes) &&
        Objects.equals(this.storageClass, v1CreateClusterRequestResourcesStorage.storageClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(diskSizeBytes, storageClass);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1CreateClusterRequestResourcesStorage {\n");
    sb.append("    diskSizeBytes: ").append(toIndentedString(diskSizeBytes)).append("\n");
    sb.append("    storageClass: ").append(toIndentedString(storageClass)).append("\n");
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

