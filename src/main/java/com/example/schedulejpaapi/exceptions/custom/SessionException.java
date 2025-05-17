package com.example.schedulejpaapi.exceptions.custom;

public class SessionException extends RuntimeException{
    public SessionException(String message) {
        super(message);
    }
}
