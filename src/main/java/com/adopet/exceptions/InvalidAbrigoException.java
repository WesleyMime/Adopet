package com.adopet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class InvalidAbrigoException extends ErrorResponseException {

    public InvalidAbrigoException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
