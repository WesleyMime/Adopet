package com.adopet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class InvalidPetException extends ErrorResponseException {

    public InvalidPetException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
