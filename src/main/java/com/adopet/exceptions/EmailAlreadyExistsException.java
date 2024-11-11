package com.adopet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class EmailAlreadyExistsException extends ErrorResponseException {

    public EmailAlreadyExistsException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
