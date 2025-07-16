package com.example.demo.exception;

public class TokenRefreshException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String token, String message) {
        super(String.format("Lỗi với token [%s]: %s", token, message));
    }
}
