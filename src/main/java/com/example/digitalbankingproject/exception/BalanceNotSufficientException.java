package com.example.digitalbankingproject.exception;

public class BalanceNotSufficientException extends Exception{
    private String message;

    public BalanceNotSufficientException(String message){
        super(message);
    }
}
