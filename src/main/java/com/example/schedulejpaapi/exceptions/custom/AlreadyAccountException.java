package com.example.schedulejpaapi.exceptions.custom;

public class AlreadyAccountException extends RuntimeException {
    public AlreadyAccountException(String message) {
        super(message);
    }
}
