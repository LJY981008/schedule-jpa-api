package com.example.schedulejpaapi.exception.custom;

public class AlreadyAccountException extends RuntimeException {
    public AlreadyAccountException(String message) {
        super(message);
    }
}
