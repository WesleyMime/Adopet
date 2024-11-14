package com.adopet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class InvalidTutorException extends ErrorResponseException {

    public InvalidTutorException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
