package com.gl.eirs.iplocation.validation;

import com.gl.eirs.iplocation.dto.IpInformation;
import com.gl.eirs.iplocation.exceptions.GlobalExceptionHandler;
import com.gl.eirs.iplocation.exceptions.MissingRequestParameterException;
import com.gl.eirs.iplocation.exceptions.UnprocessableEntityException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Service
public class RequestValidation {

    @Autowired
    GlobalExceptionHandler globalExceptionHandler;
    public static final HttpMethod GET = HttpMethod.valueOf("GET");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public void validateRequest(HttpServletRequest request, IpInformation ipInformation) {
        logger.info("Validating the request received for check ip location.");

        if(!request.getRequestURI().equalsIgnoreCase("/eirs/services/checkIPCountry")) {
            logger.error("The requested method {}, is not supported", request.getRequestURI());
            HttpMethod httpMethod = HttpMethod.valueOf(request.getMethod());
            globalExceptionHandler.handleNoResourceFoundException(new NoResourceFoundException(httpMethod ,"The requested url " + request.getRequestURL().toString()+ ", is not supported"));
        }
        if(!request.getMethod().equalsIgnoreCase("POST")) {
            logger.error("The requested method {}, is not supported", request.getMethod());
            globalExceptionHandler.handleMethodNotAllowed(new HttpRequestMethodNotSupportedException("The requested method " + request.getMethod()+ ", is not supported"));
        }

        if(!request.getHeader("content-type").equalsIgnoreCase("application/json")) {
            logger.error("The content-type received is {}, not supported.", request.getHeader("content-type"));
            globalExceptionHandler.handleHttpMediaTypeNotSupported(new HttpMediaTypeNotSupportedException("The content-type received is not supported"));
        }

        if(ipInformation.getIp() == null) {
            logger.error("Ip is missing the request");
            throw new MissingRequestParameterException("IP is missing");
        }
        if(ipInformation.getIpType() == null) {
            logger.error("Ip type is missing the request");
            throw new MissingRequestParameterException("IP type is missing");
        }

        if(ipInformation.getIp().isEmpty() || ipInformation.getIp().isBlank()) {
            logger.error("Ip value is null or blank");
            throw new UnprocessableEntityException("IP value is null");
        }

        if(ipInformation.getIpType().isEmpty() || ipInformation.getIpType().isBlank()) {
            logger.error("Ip Type value is null or blank");
            throw new UnprocessableEntityException("IP Type value is null");
        }
    }
}
