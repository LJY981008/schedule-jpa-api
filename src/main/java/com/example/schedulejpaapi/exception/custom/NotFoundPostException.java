package com.example.schedulejpaapi.exception.custom;

public class NotFoundPostException extends RuntimeException{
    public NotFoundPostException(String message) {
        super(message);
    }
}
