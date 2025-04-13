package com.example.demo.exception;

public class AuthorAccessDeniedException extends RuntimeException {
    public AuthorAccessDeniedException(String message) {
        super(message);
    }
}
