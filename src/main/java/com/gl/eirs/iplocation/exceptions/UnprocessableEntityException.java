package com.gl.eirs.iplocation.exceptions;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException{

    String message;

    public UnprocessableEntityException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return "UnprocessableEntityException{" +
                "message='" + message + '\'' +
                '}';
    }
}
