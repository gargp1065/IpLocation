package com.gl.eirs.iplocation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gl.eirs.iplocation.dto.IpInformation;
import com.gl.eirs.iplocation.exceptions.GlobalExceptionHandler;
import com.gl.eirs.iplocation.service.CheckIpLocationService;
import com.gl.eirs.iplocation.service.CheckIpLocationUtils;
import com.gl.eirs.iplocation.validation.RequestValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckIpLocation {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    CheckIpLocationService checkIpLocationService;
    @Autowired
    CheckIpLocationUtils checkIpLocationUtils;
    @Autowired
    HttpServletRequest request;

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;
    @Autowired
    RequestValidation requestValidation;
    @PostMapping("/services/checkIpCountry")
    public ResponseEntity<Object> checkIpLocation(@RequestBody IpInformation ipInformation) throws JsonProcessingException {
        logger.info("Received request for check ip country: {}", ipInformation.toString());
        ResponseEntity<Object> response = checkIpLocationService.getIpCountry(request, ipInformation);
        logger.info("The response for request received is {}" , response);
        return response;
    }



}
