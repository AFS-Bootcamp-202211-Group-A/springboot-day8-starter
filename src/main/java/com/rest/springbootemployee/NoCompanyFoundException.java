package com.rest.springbootemployee;
public class NoCompanyFoundException extends RuntimeException {

    public NoCompanyFoundException() {
        super("No company found");
    }
}
