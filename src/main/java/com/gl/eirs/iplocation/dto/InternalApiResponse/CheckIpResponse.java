package com.gl.eirs.iplocation.dto.InternalApiResponse;

import lombok.Data;

@Data
public class CheckIpResponse implements IApiResponse {

    int statusCode;
    String statusMessage;

    public CheckIpResponse(int statusCode, String statusMsg, String countryCode, String countryName) {
        this.statusCode = statusCode;
        this.statusMessage = statusMsg;
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    String countryCode;
    String countryName;
}
