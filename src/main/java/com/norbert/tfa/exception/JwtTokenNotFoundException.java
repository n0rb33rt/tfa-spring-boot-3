package com.norbert.tfa.exception;

public class JwtTokenNotFoundException extends RuntimeException{
    public JwtTokenNotFoundException(String message) {
        super(message);
    }
}
