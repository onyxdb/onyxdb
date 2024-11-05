package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * V1CreateClusterRequestDbSpecMongodb
 */

@JsonTypeName("V1CreateClusterRequest_dbSpec_mongodb")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class V1CreateClusterRequestDbSpecMongodb {

  /**
   * Gets or Sets version
   */
  public enum VersionEnum {
    _6_0("6.0"),
    
    _5_0("5.0");

    private String value;

    VersionEnum(String value) {
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
    public static VersionEnum fromValue(String value) {
      for (VersionEnum b : VersionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private VersionEnum version;

  @Valid
  private Map<String, String> mongodbV6x0 = new HashMap<>();

  @Valid
  private Map<String, String> mongodbV5x0 = new HashMap<>();

  public V1CreateClusterRequestDbSpecMongodb() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public V1CreateClusterRequestDbSpecMongodb(VersionEnum version) {
    this.version = version;
  }

  public V1CreateClusterRequestDbSpecMongodb version(VersionEnum version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
  */
  @NotNull 
  @Schema(name = "version", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  public VersionEnum getVersion() {
    return version;
  }

  public void setVersion(VersionEnum version) {
    this.version = version;
  }

  public V1CreateClusterRequestDbSpecMongodb mongodbV6x0(Map<String, String> mongodbV6x0) {
    this.mongodbV6x0 = mongodbV6x0;
    return this;
  }

  public V1CreateClusterRequestDbSpecMongodb putMongodbV6x0Item(String key, String mongodbV6x0Item) {
    if (this.mongodbV6x0 == null) {
      this.mongodbV6x0 = new HashMap<>();
    }
    this.mongodbV6x0.put(key, mongodbV6x0Item);
    return this;
  }

  /**
   * Get mongodbV6x0
   * @return mongodbV6x0
  */
  
  @Schema(name = "mongodbV6x0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mongodbV6x0")
  public Map<String, String> getMongodbV6x0() {
    return mongodbV6x0;
  }

  public void setMongodbV6x0(Map<String, String> mongodbV6x0) {
    this.mongodbV6x0 = mongodbV6x0;
  }

  public V1CreateClusterRequestDbSpecMongodb mongodbV5x0(Map<String, String> mongodbV5x0) {
    this.mongodbV5x0 = mongodbV5x0;
    return this;
  }

  public V1CreateClusterRequestDbSpecMongodb putMongodbV5x0Item(String key, String mongodbV5x0Item) {
    if (this.mongodbV5x0 == null) {
      this.mongodbV5x0 = new HashMap<>();
    }
    this.mongodbV5x0.put(key, mongodbV5x0Item);
    return this;
  }

  /**
   * Get mongodbV5x0
   * @return mongodbV5x0
  */
  
  @Schema(name = "mongodbV5x0", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mongodbV5x0")
  public Map<String, String> getMongodbV5x0() {
    return mongodbV5x0;
  }

  public void setMongodbV5x0(Map<String, String> mongodbV5x0) {
    this.mongodbV5x0 = mongodbV5x0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    V1CreateClusterRequestDbSpecMongodb v1CreateClusterRequestDbSpecMongodb = (V1CreateClusterRequestDbSpecMongodb) o;
    return Objects.equals(this.version, v1CreateClusterRequestDbSpecMongodb.version) &&
        Objects.equals(this.mongodbV6x0, v1CreateClusterRequestDbSpecMongodb.mongodbV6x0) &&
        Objects.equals(this.mongodbV5x0, v1CreateClusterRequestDbSpecMongodb.mongodbV5x0);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, mongodbV6x0, mongodbV5x0);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1CreateClusterRequestDbSpecMongodb {\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    mongodbV6x0: ").append(toIndentedString(mongodbV6x0)).append("\n");
    sb.append("    mongodbV5x0: ").append(toIndentedString(mongodbV5x0)).append("\n");
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

