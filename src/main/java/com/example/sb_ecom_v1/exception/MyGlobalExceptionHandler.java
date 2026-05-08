package com.example.sb_ecom_v1.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // will intercept any exception that are thrown by any controller in the application (Global Exception Intercepter)

public class MyGlobalExceptionHandler {
    // Any exception that occurs this class will handle


    /**
     * Example: Client sends { "categoryName": "" }
     * Response: { "categoryName": "must not be blank" }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity <Map<String,String>> handleValidationExceptions(MethodArgumentNotValidException e){

        // Line 1: Create an empty Map/HashMap to store field names and error messages
        Map<String, String> response = new HashMap<>();

        // Line 2: Get ALL validation errors from the exception
        e.getBindingResult().getAllErrors().forEach((error) -> {

            // Line 3: Extract the field name that failed validation (e.g., "categoryName")
            String filedName = ((FieldError) error).getField();

            // Line 4: Extract the error message (e.g., "must not be blank")
            String message = error.getDefaultMessage();

            // Line 5: Put the pair (fieldName -> errorMessage) into the map
            response.put(filedName, message);
        });

        // Line 6: Return the map as JSON response
        return  new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
    }
}
