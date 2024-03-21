package com.gl.eirs.iplocation.exceptions;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class MissingRequestParameterException extends RuntimeException{

    String message;

    public MissingRequestParameterException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return "MissingRequestParameterException{" +
                "message='" + message + '\'' +
                '}';
    }
}
