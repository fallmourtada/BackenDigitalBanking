package com.example.digitalbankingproject.repository;

import com.example.digitalbankingproject.entite.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface AccountOperationRepository extends JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByBankAccountId(Long accountId);

    Page<AccountOperation> findByBankAccount_Id(Long bankAccountId, Pageable pageable);

}
