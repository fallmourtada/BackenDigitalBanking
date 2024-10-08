package com.example.digitalbankingproject.dto;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {
    private Long id;

    private List<AccountOperationDTO> accountOperationDTOS;

    private double balance;

    private int currentPage;

    private int totalPages;
    private int size;
}
