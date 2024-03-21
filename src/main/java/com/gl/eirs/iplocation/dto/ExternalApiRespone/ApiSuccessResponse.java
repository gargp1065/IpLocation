package com.gl.eirs.iplocation.dto.ExternalApiRespone;


import lombok.Data;
import lombok.Getter;


@Data
public class ApiSuccessResponse implements ApiResponse {

    String ip;
    String country_code;
    String country_name;
    String region_name;
    String city_name;
    String latitude;
    String longitude;
    String zip_code;
    String time_zone;
    String asn;
    String as;
    String is_proxy;

}
