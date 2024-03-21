package com.gl.eirs.iplocation.dto.ExternalApiRespone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiErrorResponse implements ApiResponse{

    private ErrorResponse error;

    public ApiErrorResponse(ErrorResponse error) {
        this.error = error;
    }

    public ApiErrorResponse() {

    }
}

