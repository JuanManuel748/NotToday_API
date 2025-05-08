package com.github.juanmanuel.nottodaytomorrow.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{
    private String excepDetails;
    private Object fieldValue;

    public NotFoundException(String excepDetails, Object fieldValue){
        super(excepDetails + " - " + fieldValue);
        this.excepDetails = excepDetails;
        this.fieldValue = fieldValue;
    }

    public NotFoundException() {}

    public String getExcepDetails(){
        return excepDetails;
    }

    public Object getFieldValue(){
        return fieldValue;
    }
}
