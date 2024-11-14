package com.adopet.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

public class PetAlreadyAdoptedException extends ErrorResponseException {

    public PetAlreadyAdoptedException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
