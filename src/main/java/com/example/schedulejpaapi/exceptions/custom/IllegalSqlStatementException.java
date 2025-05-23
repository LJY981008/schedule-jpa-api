package com.example.schedulejpaapi.exceptions.custom;

public class IllegalSqlStatementException extends RuntimeException{
    public IllegalSqlStatementException(String message) {
        super(message);
    }
}
