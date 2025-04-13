package com.example.demo.exception;

public class LimitationException extends RuntimeException {
    public LimitationException(String message) {
        super(message);
    }
}
