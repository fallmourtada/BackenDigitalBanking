package com.example.digitalbankingproject.dto;

import com.example.digitalbankingproject.enumeration.AccountSatus;
import lombok.Data;

import java.util.Date;

@Data
public class SaveSavingAccountDTO {
    private Long id;

    private double interestRate;

    private double balance;

    private AccountSatus accountStatus;

    private Date createdAt;

    private Long customerId;
}
