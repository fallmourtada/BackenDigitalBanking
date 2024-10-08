package com.example.digitalbankingproject.dto;

import lombok.Data;

@Data
public class CreditDTO {
    private Long accountId;

    private double amount;

    private String description;
}
