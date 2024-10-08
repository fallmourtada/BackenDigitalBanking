package com.example.digitalbankingproject.exception;

public class BankAccountNotFoundExcetion extends Exception{
    private String message;
    public BankAccountNotFoundExcetion(String message){
        super(message);
    }
}
