package com.gl.eirs.iplocation.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.eirs.iplocation.dto.ExternalApiRespone.ApiErrorResponse;
import com.gl.eirs.iplocation.dto.ExternalApiRespone.ApiResponse;
import com.gl.eirs.iplocation.dto.ExternalApiRespone.ApiSuccessResponse;
import com.gl.eirs.iplocation.dto.ExternalApiRespone.ErrorResponse;
import com.gl.eirs.iplocation.dto.InternalApiResponse.CheckIpResponse;
import com.gl.eirs.iplocation.dto.InternalApiResponse.ExceptionResponse;
import com.gl.eirs.iplocation.dto.InternalApiResponse.IApiResponse;
import com.gl.eirs.iplocation.dto.IpInformation;
import com.gl.eirs.iplocation.entity.app.Ipv4;
import com.gl.eirs.iplocation.entity.app.Ipv6;
import com.gl.eirs.iplocation.exceptions.GlobalExceptionHandler;
import com.gl.eirs.iplocation.exceptions.MissingRequestParameterException;
import com.gl.eirs.iplocation.exceptions.UnprocessableEntityException;
import com.gl.eirs.iplocation.repository.app.Ipv4Repository;
import com.gl.eirs.iplocation.repository.app.Ipv6Repository;
import com.gl.eirs.iplocation.repository.app.SysParamRepository;
import com.gl.eirs.iplocation.validation.RequestValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

import static com.gl.eirs.iplocation.constants.Constants.ipType4;
import static com.gl.eirs.iplocation.constants.Constants.ipType6;
import static com.gl.eirs.iplocation.constants.Constants.success200Msg;
import static com.gl.eirs.iplocation.constants.Constants.success201Msg;
import static com.gl.eirs.iplocation.constants.Constants.success202Msg;


@Service
public class CheckIpLocationService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CheckIpLocationUtils checkIpLocationUtils;
    @Autowired
    RequestValidation requestValidation;
    @Autowired
    Ipv4Repository ipv4Repository;

    @Autowired
    SysParamRepository sysParamRepository;

    @Autowired
    DbConfigService dbConfigService;

    @Autowired
    Ipv6Repository ipv6Repository;
    @Autowired
    HttpServletRequest request;
    @Autowired
    GlobalExceptionHandler globalExceptionHandler;
    public ResponseEntity<Object> getIpCountry(HttpServletRequest request, IpInformation ipInformation) throws JsonProcessingException {
        try {
            requestValidation.validateRequest(request, ipInformation);
            logger.info("Validation of the request is successful.");
            String ipNumber;
            IApiResponse iApiResponse;
            if (ipInformation.getIpType().equalsIgnoreCase(ipType4)) {
                // get number for ip
                logger.info("The ip type received is IPv4");
                ipNumber = checkIpLocationUtils.convertIpv4ToNumber(ipInformation.getIp());
                // search in db
                logger.info("Ip number for ipv4 is {}", ipNumber);
                Ipv4 ipv4 = ipv4Repository.findByIpRange(Long.parseLong(ipNumber));
                if (ipv4 == null) {
                    // not found in db.
                    // call api and check the details
                    logger.info("The ip number {} is not found in db", ipNumber);
                    iApiResponse = callApiCheck(ipInformation.getIp());
                    if (iApiResponse instanceof ExceptionResponse) {
                        return ResponseEntity.status(500).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
                    } else {
                        logger.info("Ip number {} found in api call, country code {} and country name {}", ipNumber,
                                ((CheckIpResponse) iApiResponse).getCountryCode(), ((CheckIpResponse) iApiResponse).getCountryName());
                        return ResponseEntity.status(200).body((CheckIpResponse) iApiResponse);
                    }
                } else {
                    logger.info("Ip number {} found in Db, country code {} and country name {}", ipNumber,
                            ipv4.getCountryName(), ipv4.getCountryCode());
                    CheckIpResponse checkIpResponse = checkCountry(ipv4.getCountryName(), ipv4.getCountryCode());
                    return ResponseEntity.status(200).body(checkIpResponse);
                }
            } else if (ipInformation.getIpType().equalsIgnoreCase(ipType6)) {
                logger.info("The ip type received is IPv6");
                ipNumber = checkIpLocationUtils.convertIpv6ToNumber(ipInformation.getIp());
                logger.info("Ip number for ipv6 is {}", ipNumber);
                Ipv6 ipv6 = ipv6Repository.findByIpRange(new BigInteger(ipNumber));
                logger.info("ipv6 is {}", ipv6);
                if (ipv6 == null) {
                    // not found in db.
                    // call api and check the details
                    logger.info("The ip number {} is not found in db", ipNumber);
                    iApiResponse = callApiCheck(ipInformation.getIp());
                    if (iApiResponse instanceof ExceptionResponse) {
                        return ResponseEntity.status(500).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
                    } else {
                        logger.info("Ip number {} found in api call, country code {} and country name {}", ipNumber,
                                ((CheckIpResponse) iApiResponse).getCountryCode(), ((CheckIpResponse) iApiResponse).getCountryName());
                        return ResponseEntity.status(200).body((CheckIpResponse) iApiResponse);
                    }
                } else {
                    logger.info("Ip number {} found in Db, country code {} and country name {}", ipNumber,
                            ipv6.getCountryName(), ipv6.getCountryCode());
                    CheckIpResponse checkIpResponse = checkCountry(ipv6.getCountryName(), ipv6.getCountryCode());
                    return ResponseEntity.status(200).body(checkIpResponse);
                }
            }
            return ResponseEntity.status(500).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        } catch (UnprocessableEntityException e) {
            return globalExceptionHandler.exception(e);
        } catch(MissingRequestParameterException e) {
            return globalExceptionHandler.exception(e);
        }
        catch (Exception e ) {
            logger.error("Error occurred while processing the request {}", e.getLocalizedMessage());
            return ResponseEntity.status(500).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }
    }


    public ApiResponse checkLocationApi(String ip) throws JsonProcessingException {
        logger.info("Calling the api for checking the ip {} in online database", ip);
        RestTemplate restTemplate = null;
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
            clientHttpRequestFactory.setConnectTimeout(1000);
            clientHttpRequestFactory.setReadTimeout(1000);
            restTemplate = new RestTemplate(clientHttpRequestFactory);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url = dbConfigService.getValue("ipCountryApiUrl");
            String token = dbConfigService.getValue("ipCountryApiKey");
            url = url.replace("<ip>", ip);
            url = url.replace("<key>", token);
            logger.info("Url is {}", url);

            ApiResponse response = restTemplate.getForEntity(url, ApiSuccessResponse.class).getBody();
            logger.info("The response from api call is {}", response);
            return response;
        } catch (HttpClientErrorException e) {
            ApiErrorResponse apiErrorResponse = objectMapper.readValue(e.getResponseBodyAsString(), ApiErrorResponse.class);
            logger.error("The error is {}", e.getMessage());
            return apiErrorResponse;
        } catch (Exception e) {
            logger.error("Exception in request: {}", e.getMessage());
            return new ApiErrorResponse(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }
    }

    public IApiResponse callApiCheck(String ip) throws JsonProcessingException {
        logger.info("Calling the ip location api url as ip not found in DB");
        ApiResponse apiResponse = checkLocationApi(ip);

        if(apiResponse instanceof ApiSuccessResponse) {
            logger.info("Api response is successful.");
            return checkCountry(((ApiSuccessResponse) apiResponse).getCountry_name(),
                    ((ApiSuccessResponse) apiResponse).getCountry_code());

        }
        else {
            logger.info("Error in api response {}", apiResponse);
            return new ExceptionResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
            );
        }

    }
    public CheckIpResponse checkCountry(String countryName, String countryCode) {
        if(countryCode.equalsIgnoreCase("KH")) {
            return new CheckIpResponse(HttpStatus.OK.value(), success200Msg, countryCode, countryName);
        }
        else if(countryCode.equalsIgnoreCase("-")) {
            return new CheckIpResponse(HttpStatus.ACCEPTED.value(), success202Msg, "KH", "Cambodia");
        }
        else return new CheckIpResponse(HttpStatus.CREATED.value(), success201Msg, countryCode, countryName);
    }

}
