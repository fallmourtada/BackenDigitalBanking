package com.example.digitalbankingproject.dto;

import com.example.digitalbankingproject.enumeration.AccountSatus;
import lombok.Data;

import java.util.Date;

@Data
public class BankAccountDTO {
    private Long id;

    private double balance;

    private Date createdAt;

    private AccountSatus status;

    private CustomerDTO customerDTO;

    private String type;
}
