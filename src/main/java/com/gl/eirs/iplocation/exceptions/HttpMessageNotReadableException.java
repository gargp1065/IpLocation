package com.gl.eirs.iplocation.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class HttpMessageNotReadableException extends RuntimeException{

    String message;

    public HttpMessageNotReadableException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return "HttpMessageNotReadableException{" +
                "message='" + message + '\'' +
                '}';
    }
}
