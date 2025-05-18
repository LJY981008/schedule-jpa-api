package com.example.schedulejpaapi.exceptions.custom;

public class InvalidFieldException extends RuntimeException{
    public InvalidFieldException(String message) {
        super(message);
    }
}
