package com.example.schedulejpaapi.exception.custom;

public class IllegalSqlStatementException extends RuntimeException{
    public IllegalSqlStatementException(String message) {
        super(message);
    }
}
