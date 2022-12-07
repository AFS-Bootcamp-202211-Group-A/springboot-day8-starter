package com.rest.springbootemployee;

public class ResponseData {
    private String status;
    private String message;

    public ResponseData(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
