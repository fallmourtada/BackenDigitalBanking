package com.example.digitalbankingproject.controller;

import com.example.digitalbankingproject.dto.*;
import com.example.digitalbankingproject.exception.BalanceNotSufficientException;
import com.example.digitalbankingproject.exception.BankAccountNotFoundExcetion;
import com.example.digitalbankingproject.exception.CustomerNotFoundException;
import com.example.digitalbankingproject.service.BankAccountService.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class BankAccountController {
    private BankAccountService bankAccountService;

    @GetMapping("/bankAccount/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable Long accountId) throws BankAccountNotFoundExcetion {
        return bankAccountService.getBankAccount(accountId);
    }


    @GetMapping("/bankAccounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.listAccounts();
    }

    @GetMapping("/accounts/{accounId}/operations")
    public List<AccountOperationDTO> getHistory(Long accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @PostMapping("/accounts/compteCourant")
    public CurrentAccountDTO saveCompteCourant(@RequestBody SaveCurrentAccountDTO saveCurrentAccountDTO) throws CustomerNotFoundException {
        return bankAccountService.CreerCompteCourant(saveCurrentAccountDTO);
    }

    @PostMapping("/accounts/compteEpargne")
    public SavingAccountDTO saveCompteEpargne(@RequestBody SaveSavingAccountDTO saveSavingAccountDTO) throws CustomerNotFoundException {
        return bankAccountService.CreerCompteEpargne(saveSavingAccountDTO);
    }


    @PostMapping("/accounts/crediter/{accountId}")
    public void crediterCompte(@PathVariable Long accountId,@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundExcetion {
        bankAccountService.crediterCompte(accountId,creditDTO);
    }

    @PostMapping("/accounts/debiter/{accountId}")
    public void debiterCompte(@PathVariable Long accountId,@RequestBody DebitDTO debitDTO) throws BalanceNotSufficientException {
        bankAccountService.debiterCompte(accountId,debitDTO);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable Long accountId,
                                                     @RequestParam(name = "page",defaultValue = "0") int page,
                                                     @RequestParam(name="size",defaultValue = "5") int size) throws BankAccountNotFoundExcetion {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

    @PostMapping("/accounts/transfert/{accountIdSource}/{accountIdDestination}")
    public void transfert(@PathVariable  Long accountIdSource,@PathVariable Long accountIdDestination,TransfertDTO transfertDTO) throws BankAccountNotFoundExcetion, BalanceNotSufficientException {
        bankAccountService.transfert(accountIdSource,accountIdDestination,transfertDTO);
    }

    @PostMapping(value = "/accounts/transfertSimplifier/{accountIdSource}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void transfertSimplifier(@PathVariable Long accountIdSource,@RequestBody TransfertDTO transfertDTO) throws BankAccountNotFoundExcetion, BalanceNotSufficientException {
        bankAccountService.transfertSimplifier(accountIdSource,transfertDTO);
    }

    @GetMapping("/bankAccount/customers/{customerId}")
    public List<BankAccountDTO> findBankAccountByCustomerId(@PathVariable Long customerId){
        return bankAccountService.findBankAccountByCustomerId(customerId);

    }
}