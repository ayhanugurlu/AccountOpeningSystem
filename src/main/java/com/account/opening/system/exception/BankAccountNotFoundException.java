package com.account.opening.system.exception;

public class BankAccountNotFoundException extends RuntimeException{
    public BankAccountNotFoundException(String message) {
        super(message);
    }
}
