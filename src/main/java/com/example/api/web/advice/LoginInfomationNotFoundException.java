package com.example.api.web.advice;

public class LoginInfomationNotFoundException extends RuntimeException {
    public LoginInfomationNotFoundException(String message) {
        super(message);
    }
}
