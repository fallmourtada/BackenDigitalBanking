package com.example.digitalbankingproject.exception;

public class CustomerNotFoundException extends Exception{
    private String message;

    public CustomerNotFoundException(String message){
        super(message);
    }
}
