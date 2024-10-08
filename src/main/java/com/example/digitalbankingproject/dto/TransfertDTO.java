package com.example.digitalbankingproject.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TransfertDTO {
    private Long accountIdSource;

    private Long accountIdDestination;

    private double amount;

    private String description;
}
