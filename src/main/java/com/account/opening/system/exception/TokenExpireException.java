package com.account.opening.system.exception;

public class TokenExpireException extends RuntimeException{
    public TokenExpireException(String message) {
        super(message);
    }
}
