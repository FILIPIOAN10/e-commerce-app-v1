package com.example.sb_ecom_v1.exceptions;

public class APIException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    public APIException() {

    }
    public APIException(String message) {
        super(message);
    }
}
