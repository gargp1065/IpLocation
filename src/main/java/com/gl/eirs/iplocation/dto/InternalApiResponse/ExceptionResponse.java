package com.gl.eirs.iplocation.dto.InternalApiResponse;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class ExceptionResponse implements IApiResponse {

    int statusCode;
    String statusMessage;
//    String countryCode;
//    String countryName;


    public ExceptionResponse(int statusCode, String errorMsg) {
        this.statusCode = statusCode;
        this.statusMessage = errorMsg;
    }
}
