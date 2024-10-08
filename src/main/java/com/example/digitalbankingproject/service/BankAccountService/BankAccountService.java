package com.example.digitalbankingproject.service.BankAccountService;

import com.example.digitalbankingproject.dto.*;
import com.example.digitalbankingproject.exception.BalanceNotSufficientException;
import com.example.digitalbankingproject.exception.BankAccountNotFoundExcetion;
import com.example.digitalbankingproject.exception.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {


    List<CustomerDTO> listCustomers();

    BankAccountDTO getBankAccount(Long accountId)throws BankAccountNotFoundExcetion;


    void transfertSimplifier(Long accountIdSource,TransfertDTO transfertDTO) throws BankAccountNotFoundExcetion, BalanceNotSufficientException;

    void transfert(Long accountIdSource,Long accountIdDestination,TransfertDTO transfertDTO) throws BankAccountNotFoundExcetion, BalanceNotSufficientException;

    SavingAccountDTO saveSavingAccount(double initialBalance,double interestRate,Long customerId)throws CustomerNotFoundException;

    CurrentAccountDTO saveCurrentAccount(double initialBalance,double overDraft,Long customerId)throws CustomerNotFoundException;

    List<BankAccountDTO> listAccounts();

    List<AccountOperationDTO> accountHistory(Long accountId);

    void debiterCompte(Long accountId,DebitDTO debitDTO) throws BalanceNotSufficientException;

    void crediterCompte(Long accountId,CreditDTO creditDTO) throws BankAccountNotFoundExcetion;

    SavingAccountDTO CreerCompteEpargne(SaveSavingAccountDTO savingAccountDTO) throws CustomerNotFoundException;

    CurrentAccountDTO CreerCompteCourant(SaveCurrentAccountDTO saveCurrentAccountDTO) throws CustomerNotFoundException;

    AccountHistoryDTO getAccountHistory(Long bankAccountId, int page, int size) throws BankAccountNotFoundExcetion;

    List<BankAccountDTO> findBankAccountByCustomerId(Long customerId);


}
