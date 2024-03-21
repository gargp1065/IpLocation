package com.gl.eirs.iplocation.dto.ExternalApiRespone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class ErrorResponse {
    

    @JsonProperty("error_code")
    int error_code;

    @JsonProperty("error_message")
    String error_message;

    public ErrorResponse(int error_code, String error_message) {
        this.error_code = error_code;
        this.error_message = error_message;
    }
    public ErrorResponse() {

    }


// Getters and setters
}
