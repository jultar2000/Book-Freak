package com.example.Authmodule.exceptions;

public class EmailException extends RuntimeException {
    public EmailException(String message, Exception exception) {
        super(message, exception);
    }
}
