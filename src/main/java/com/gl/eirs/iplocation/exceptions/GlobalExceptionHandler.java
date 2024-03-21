package com.gl.eirs.iplocation.exceptions;

import com.gl.eirs.iplocation.dto.InternalApiResponse.ExceptionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(value = MissingRequestParameterException.class)
    public ResponseEntity<Object> exception(MissingRequestParameterException exception) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()), HttpStatus.BAD_REQUEST)
        ;
    }

    @ExceptionHandler(value = UnprocessableEntityException.class)
    public ResponseEntity<Object> exception(UnprocessableEntityException exception) {
        return new ResponseEntity<>(
                new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), exception.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY)
                ;
    }

    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ExceptionResponse handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)// other than json
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse handleHttpMediaTypeNotSupported(Exception e) {
        return new ExceptionResponse(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage());
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNoResourceFoundException(NoResourceFoundException e) {
        logger.error("No resources found");
        return new ExceptionResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

}
