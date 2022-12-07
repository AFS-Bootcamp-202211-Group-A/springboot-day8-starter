package com.rest.springbootemployee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoEmployeeFoundException.class, NoCompanyFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void method() {
    }
}
