package com.example.digitalbankingproject.mapper;

import com.example.digitalbankingproject.dto.BankAccountDTO;
import com.example.digitalbankingproject.dto.CurrentAccountDTO;
import com.example.digitalbankingproject.dto.CustomerDTO;
import com.example.digitalbankingproject.dto.SavingAccountDTO;
import com.example.digitalbankingproject.entite.BankAccount;
import com.example.digitalbankingproject.entite.CurrentAccount;
import com.example.digitalbankingproject.entite.Customer;
import com.example.digitalbankingproject.entite.SavingAccount;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BankAccountMapper {
    private CustomerMapper customerMapper;

    public SavingAccount fromSavingAccountDTO(SavingAccountDTO savingAccountDTO){
        SavingAccount savingAccount=new SavingAccount();
        BeanUtils.copyProperties(savingAccountDTO,savingAccount);
        savingAccount.setCustomer(customerMapper.fromCustomerDTO(savingAccountDTO.getCustomerDTO()));
        return savingAccount;
    }

    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount){
        SavingAccountDTO savingAccountDTO=new SavingAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingAccountDTO);
        savingAccountDTO.setCustomerDTO(customerMapper.fromCustomer(savingAccount.getCustomer()));
        savingAccountDTO.setType(savingAccount.getClass().getSimpleName());
        return savingAccountDTO;
    }

    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount){
        CurrentAccountDTO currentAccountDTO=new CurrentAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentAccountDTO);
        currentAccountDTO.setCustomerDTO(customerMapper.fromCustomer(currentAccount.getCustomer()));
        currentAccountDTO.setType(currentAccount.getClass().getSimpleName());
        return currentAccountDTO;
    }

    public CurrentAccount fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentAccountDTO,currentAccount);
        currentAccount.setCustomer(customerMapper.fromCustomerDTO(currentAccountDTO.getCustomerDTO()));
        return currentAccount;
    }

    public BankAccount fromBankAccountDTO(BankAccountDTO bankAccountDTO){
        BankAccount bankAccount=new BankAccount();
        BeanUtils.copyProperties(bankAccountDTO,bankAccount);
        bankAccount.setCustomer(customerMapper.fromCustomerDTO(bankAccountDTO.getCustomerDTO()));
        return bankAccount;
    }

    public BankAccountDTO fromBankAccount(BankAccount bankAccount){
        BankAccountDTO bankAccountDTO=new BankAccountDTO();
        BeanUtils.copyProperties(bankAccount,bankAccountDTO);
        bankAccountDTO.setCustomerDTO(customerMapper.fromCustomer(bankAccount.getCustomer()));
        return bankAccountDTO;
    }


}
