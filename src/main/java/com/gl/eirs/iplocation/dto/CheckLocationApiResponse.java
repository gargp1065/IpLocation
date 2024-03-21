package com.gl.eirs.iplocation.dto;


import lombok.Data;

@Data
public class CheckLocationApiResponse {

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
