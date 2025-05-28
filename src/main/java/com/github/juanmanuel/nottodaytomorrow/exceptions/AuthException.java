package com.github.juanmanuel.nottodaytomorrow.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AuthException extends RuntimeException {
    private String excepDetails;
    private Object fieldValue;

    public AuthException(String excepDetails, Object fieldValue) {
        super(excepDetails + " - " + fieldValue);
        this.excepDetails = excepDetails;
        this.fieldValue = fieldValue;
    }

    public AuthException(String message) {
        super(message);
        this.excepDetails = message;
    }

    public AuthException() {}

    public String getExcepDetails() {
        return excepDetails;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}