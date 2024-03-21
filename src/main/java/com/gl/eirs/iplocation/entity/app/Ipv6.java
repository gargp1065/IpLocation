package com.gl.eirs.iplocation.entity.app;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Entity
@Data
@Table(name="ip_location_country_ipv6")
public class Ipv6 {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="start_ip_number")
    BigInteger startIpNumber;


    @Column(name="end_ip_number")
    BigInteger endIpNumber;

    @Column(name="country_code")
    String countryCode;

    @Column(name="country_name")
    String countryName;

    @Column(name="data_source")
    String dataSource;

}
