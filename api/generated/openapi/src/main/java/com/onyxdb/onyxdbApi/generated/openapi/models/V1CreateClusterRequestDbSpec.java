package com.onyxdb.onyxdbApi.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.onyxdb.onyxdbApi.generated.openapi.models.V1CreateClusterRequestDbSpecMongodb;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * V1CreateClusterRequestDbSpec
 */

@JsonTypeName("V1CreateClusterRequest_dbSpec")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class V1CreateClusterRequestDbSpec {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    MONGODB("mongodb"),
    
    POSTGRESQL("postgresql");

    private String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private V1CreateClusterRequestDbSpecMongodb mongodb;

  public V1CreateClusterRequestDbSpec() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public V1CreateClusterRequestDbSpec(TypeEnum type) {
    this.type = type;
  }

  public V1CreateClusterRequestDbSpec type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public V1CreateClusterRequestDbSpec mongodb(V1CreateClusterRequestDbSpecMongodb mongodb) {
    this.mongodb = mongodb;
    return this;
  }

  /**
   * Get mongodb
   * @return mongodb
  */
  @Valid 
  @Schema(name = "mongodb", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mongodb")
  public V1CreateClusterRequestDbSpecMongodb getMongodb() {
    return mongodb;
  }

  public void setMongodb(V1CreateClusterRequestDbSpecMongodb mongodb) {
    this.mongodb = mongodb;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    V1CreateClusterRequestDbSpec v1CreateClusterRequestDbSpec = (V1CreateClusterRequestDbSpec) o;
    return Objects.equals(this.type, v1CreateClusterRequestDbSpec.type) &&
        Objects.equals(this.mongodb, v1CreateClusterRequestDbSpec.mongodb);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, mongodb);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1CreateClusterRequestDbSpec {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    mongodb: ").append(toIndentedString(mongodb)).append("\n");
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

