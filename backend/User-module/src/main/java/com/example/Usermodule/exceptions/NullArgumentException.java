package com.example.Usermodule.exceptions;

public class NullArgumentException extends RuntimeException{
    public NullArgumentException(String message, Exception exception) {
        super(message, exception);
    }

    public NullArgumentException(String message) {
        super(message);
    }
}
