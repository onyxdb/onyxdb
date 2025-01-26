package com.onyxdb.idm.controllers.v1;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Setter;


/**
 * @author ArtemFed
 */
@Setter
@Schema(name = "ErrorResponse")
public class ErrorResponse {
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorResponse errorResponse = (ErrorResponse) o;
        return Objects.equals(this.message, errorResponse.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        return "class ErrorResponse {\n" +
                " message: " + toIndentedString(message) + "\n" +
                "}";
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}