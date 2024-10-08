package com.example.digitalbankingproject.dto;

import com.example.digitalbankingproject.enumeration.AccountSatus;
import lombok.Data;

import java.util.Date;

@Data
public class SaveCurrentAccountDTO {
    private Long id;

    private double overDraft;

    private double balance;

    private AccountSatus accountStatus;

    private Date createdAt;

    private Long customerId;
}
