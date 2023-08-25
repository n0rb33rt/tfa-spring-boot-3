package com.norbert.tfa.exception;


public class BadRegistrationRequest extends RuntimeException{
    public BadRegistrationRequest(String message) {
        super(message);
    }
}
