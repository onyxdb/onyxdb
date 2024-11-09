package com.onyxdb.mdb.generated.openapi.models;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.onyxdb.mdb.generated.openapi.models.V1CreateClusterRequestSpecMongodb;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * V1CreateClusterRequestSpec
 */

@JsonTypeName("V1CreateClusterRequest_spec")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.5.0")
public class V1CreateClusterRequestSpec {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    MONGODB("mongodb");

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

  private V1CreateClusterRequestSpecMongodb mongodb;

  public V1CreateClusterRequestSpec() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public V1CreateClusterRequestSpec(TypeEnum type) {
    this.type = type;
  }

  public V1CreateClusterRequestSpec type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  */
  @NotNull 
  @Schema(name = "type", example = "mongodb", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public V1CreateClusterRequestSpec mongodb(V1CreateClusterRequestSpecMongodb mongodb) {
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
  public V1CreateClusterRequestSpecMongodb getMongodb() {
    return mongodb;
  }

  public void setMongodb(V1CreateClusterRequestSpecMongodb mongodb) {
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
    V1CreateClusterRequestSpec v1CreateClusterRequestSpec = (V1CreateClusterRequestSpec) o;
    return Objects.equals(this.type, v1CreateClusterRequestSpec.type) &&
        Objects.equals(this.mongodb, v1CreateClusterRequestSpec.mongodb);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, mongodb);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class V1CreateClusterRequestSpec {\n");
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

