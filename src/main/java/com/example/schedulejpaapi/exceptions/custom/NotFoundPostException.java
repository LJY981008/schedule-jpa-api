package com.example.schedulejpaapi.exceptions.custom;

public class NotFoundPostException extends RuntimeException{
    public NotFoundPostException(String message) {
        super(message);
    }
}
