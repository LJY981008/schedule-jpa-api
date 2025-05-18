package com.example.schedulejpaapi.exceptions.custom;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message) {
        super(message);
    }
}
