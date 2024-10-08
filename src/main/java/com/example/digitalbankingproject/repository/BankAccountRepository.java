package com.example.digitalbankingproject.repository;

import com.example.digitalbankingproject.entite.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    List<BankAccount> findBankAccountByCustomerId(Long customerId);
}
