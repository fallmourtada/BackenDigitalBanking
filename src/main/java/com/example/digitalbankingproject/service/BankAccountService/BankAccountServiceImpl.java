package com.example.digitalbankingproject.service.BankAccountService;

import com.example.digitalbankingproject.dto.*;
import com.example.digitalbankingproject.entite.*;
import com.example.digitalbankingproject.enumeration.AccountSatus;
import com.example.digitalbankingproject.enumeration.OperationType;
import com.example.digitalbankingproject.exception.BalanceNotSufficientException;
import com.example.digitalbankingproject.exception.BankAccountNotFoundExcetion;
import com.example.digitalbankingproject.exception.CustomerNotFoundException;
import com.example.digitalbankingproject.mapper.AccountOperationMapper;
import com.example.digitalbankingproject.mapper.BankAccountMapper;
import com.example.digitalbankingproject.mapper.CustomerMapper;
import com.example.digitalbankingproject.repository.AccountOperationRepository;
import com.example.digitalbankingproject.repository.BankAccountRepository;
import com.example.digitalbankingproject.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService{
    private CustomerRepository customerRepository;

    private BankAccountRepository bankAccountRepository;

    private AccountOperationRepository accountOperationRepository;

    private CustomerMapper customerMapper;

    private BankAccountMapper bankAccountMapper;

    private AccountOperationMapper accountOperationMapper;



    @Override
    public List<CustomerDTO> listCustomers() {
          List<Customer> customerList=customerRepository.findAll();
          List<CustomerDTO> customerDTOList=customerList.stream().map(customer -> customerMapper.fromCustomer(customer))
                  .collect(Collectors.toList());
          return customerDTOList;
    }

    @Override
    public BankAccountDTO getBankAccount(Long accountId) throws BankAccountNotFoundExcetion{
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount=(SavingAccount) bankAccount;
            return bankAccountMapper.fromSavingAccount(savingAccount);
        }else{
            CurrentAccount currentAccount=(CurrentAccount) bankAccount;
            return bankAccountMapper.fromCurrentAccount(currentAccount);
        }

    }

    @Override
    public void transfertSimplifier(Long accountIdSource, TransfertDTO transfertDTO) throws BankAccountNotFoundExcetion, BalanceNotSufficientException {
        //Appeler la methode Debiter
        DebitDTO debitDTO=new DebitDTO();
        debitDTO.setAccountId(accountIdSource);
        debitDTO.setDescription(transfertDTO.getDescription());
        debitDTO.setAmount(transfertDTO.getAmount());
        debiterCompte(accountIdSource,debitDTO);

        //Appeler la methode CrediterCompte
        CreditDTO creditDTO=new CreditDTO();
        creditDTO.setAccountId(transfertDTO.getAccountIdDestination());
        creditDTO.setDescription(transfertDTO.getDescription());
        creditDTO.setAmount(transfertDTO.getAmount());
        crediterCompte(accountIdSource, creditDTO);
        log.info("Transfert de {} effectué avec succès du compte {} vers le compte {}.", transfertDTO.getAmount(),accountIdSource, transfertDTO.getAccountIdDestination());


    }


    @Override
    public void transfert(Long accountIdSource, Long accountIdDestination, TransfertDTO transfertDTO)
            throws BankAccountNotFoundExcetion, BalanceNotSufficientException {

        // Vérifier si les comptes source et destination existent
        BankAccount bankAccountSource = bankAccountRepository.findById(accountIdSource)
                .orElseThrow(() -> new BankAccountNotFoundExcetion("Source account not found"));
        BankAccount bankAccountDestination = bankAccountRepository.findById(accountIdDestination)
                .orElseThrow(() -> new BankAccountNotFoundExcetion("Destination account not found"));

        // Vérifier si le solde est suffisant sur le compte source
        if (bankAccountSource.getBalance() < transfertDTO.getAmount()) {
            throw new BalanceNotSufficientException("Balance not sufficient on the source account");
        }

        // Débiter le compte source
        AccountOperation debitOperation = new AccountOperation();
        debitOperation.setType(OperationType.DEBIT);
        debitOperation.setOperationDate(new Date());
        debitOperation.setDescription(transfertDTO.getDescription());
        debitOperation.setAmount(transfertDTO.getAmount());
        debitOperation.setBankAccount(bankAccountSource);
        accountOperationRepository.save(debitOperation);
        bankAccountSource.setBalance(bankAccountSource.getBalance() - transfertDTO.getAmount());
        bankAccountRepository.save(bankAccountSource);

        // Créditer le compte destination
        AccountOperation creditOperation = new AccountOperation();
        creditOperation.setType(OperationType.CREDIT);
        creditOperation.setOperationDate(new Date());
        creditOperation.setDescription(transfertDTO.getDescription());
        creditOperation.setAmount(transfertDTO.getAmount());
        creditOperation.setBankAccount(bankAccountDestination);
        accountOperationRepository.save(creditOperation);
        bankAccountDestination.setBalance(bankAccountDestination.getBalance() + transfertDTO.getAmount());
        bankAccountRepository.save(bankAccountDestination);
        log.info("Transfert Effectuer Avec Success");
    }








    @Override
    public SavingAccountDTO saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException{
        Customer customer=customerRepository.findById(customerId).orElse(null);
        CustomerDTO customerDTO=customerMapper.fromCustomer(customer);
        if(customer==null)
            throw  new CustomerNotFoundException("Customer not Found");
        SavingAccountDTO savingAccountDTO=new SavingAccountDTO();
        savingAccountDTO.setBalance(initialBalance);
        savingAccountDTO.setStatus(AccountSatus.CREATED);
        savingAccountDTO.setCreatedAt(new Date());
        savingAccountDTO.setCustomerDTO(customerDTO);
        savingAccountDTO.setInterestRate(interestRate);
        SavingAccount savingAccount=bankAccountMapper.fromSavingAccountDTO(savingAccountDTO);
        SavingAccount savingAccount1=bankAccountRepository.save(savingAccount);

        return bankAccountMapper.fromSavingAccount(savingAccount1);
    }

    @Override
    public CurrentAccountDTO saveCurrentAccount(double initialBalance, double overDraft, Long customerId)throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if(customer==null)
            throw new CustomerNotFoundException("Customer Not Found");
        CustomerDTO customerDTO=customerMapper.fromCustomer(customer);
        CurrentAccountDTO currentAccountDTO=new CurrentAccountDTO();
        currentAccountDTO.setStatus(AccountSatus.CREATED);
        currentAccountDTO.setBalance(initialBalance);
        currentAccountDTO.setCreatedAt(new Date());
        currentAccountDTO.setOverDraft(overDraft);
        currentAccountDTO.setCustomerDTO(customerDTO);
        CurrentAccount currentAccount=bankAccountMapper.fromCurrentAccountDTO(currentAccountDTO);
        CurrentAccount currentAccount1 =bankAccountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentAccount(currentAccount1);
    }

    @Override
    public List<BankAccountDTO> listAccounts() {
        List<BankAccount> bankAccountList=bankAccountRepository.findAll();

        List<BankAccountDTO> bankAccountDTOList=bankAccountList.stream().map(bankAccount -> {
            if(bankAccount instanceof SavingAccount){
                SavingAccount savingAccount=(SavingAccount) bankAccount;
                return bankAccountMapper.fromSavingAccount(savingAccount);
            }else {
                CurrentAccount currentAccount=(CurrentAccount) bankAccount;
                return bankAccountMapper.fromCurrentAccount(currentAccount);
            }
        }).collect(Collectors.toList());

        return bankAccountDTOList;
    }

    public List<AccountOperationDTO> accountHistory(Long accountId){
        List<AccountOperation> accountOperationList=accountOperationRepository.findByBankAccountId(accountId);

        List<AccountOperationDTO> accountOperationDTOS=accountOperationList.stream().map(accountOperation -> accountOperationMapper.fromAccountOperation(accountOperation))
                .collect(Collectors.toList());

        return accountOperationDTOS;
    }

    @Override
    public void debiterCompte(Long accountId,DebitDTO debitDTO) throws BalanceNotSufficientException {
        BankAccount bankAccount= bankAccountRepository.findById(accountId).get();
        if(bankAccount.getBalance()<debitDTO.getAmount())
            throw new BalanceNotSufficientException("Balance Not Sufficient");

        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setOperationDate(new Date());
        accountOperation.setDescription(debitDTO.getDescription());
        accountOperation.setAmount(debitDTO.getAmount());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-debitDTO.getAmount());
        bankAccountRepository.save(bankAccount);
        log.info("Debit de {} effectué avec succès sur le compte {}. Nouveau solde : {}", accountId, bankAccount.getBalance());

    }

    @Override
    public void crediterCompte(Long accountId,CreditDTO creditDTO) throws BankAccountNotFoundExcetion {
      BankAccount bankAccount= bankAccountRepository.findById(accountId).get();
      if(bankAccount==null)
          throw new BankAccountNotFoundExcetion("Bank Account Not Found");
      AccountOperation accountOperation=new AccountOperation();
      accountOperation.setBankAccount(bankAccount);
      accountOperation.setType(OperationType.CREDIT);
      accountOperation.setDescription(creditDTO.getDescription());
      accountOperation.setAmount(creditDTO.getAmount());
      accountOperation.setOperationDate(new Date());
      accountOperationRepository.save(accountOperation);
      bankAccount.setBalance(bankAccount.getBalance()+creditDTO.getAmount());
      bankAccountRepository.save(bankAccount);
      log.info("Crédit de {} effectué avec succès sur le compte {}. Nouveau solde : {}", accountId, bankAccount.getBalance());


    }

    @Override
    public SavingAccountDTO CreerCompteEpargne(SaveSavingAccountDTO savingAccountDTO) throws CustomerNotFoundException {
        Long customerId=savingAccountDTO.getCustomerId();
        Customer customer=customerRepository.findById(customerId).get();
        if(customer==null)
            throw new CustomerNotFoundException("Customer Does not Exist");
        SavingAccountDTO savingAccountDTO1=new SavingAccountDTO();
        savingAccountDTO1.setBalance(savingAccountDTO.getBalance());
        savingAccountDTO1.setInterestRate(savingAccountDTO.getInterestRate());
        savingAccountDTO1.setCreatedAt(new Date());
        savingAccountDTO1.setCustomerDTO(customerMapper.fromCustomer(customer));
        SavingAccount savingAccount=bankAccountMapper.fromSavingAccountDTO(savingAccountDTO1);
        SavingAccount savingAccount1=bankAccountRepository.save(savingAccount);
        SavingAccountDTO savingAccountDTO2=bankAccountMapper.fromSavingAccount(savingAccount1);
        return savingAccountDTO2;
    }

    @Override
    public CurrentAccountDTO CreerCompteCourant(SaveCurrentAccountDTO saveCurrentAccountDTO) throws CustomerNotFoundException {
        Long customerId=saveCurrentAccountDTO.getCustomerId();
        Customer customer=customerRepository.findById(customerId).get();
        if(customer==null)
            throw  new CustomerNotFoundException("Customer Does not Exist");
        CurrentAccountDTO currentAccountDTO=new CurrentAccountDTO();
        currentAccountDTO.setStatus(AccountSatus.CREATED);
        currentAccountDTO.setBalance(saveCurrentAccountDTO.getBalance());
        currentAccountDTO.setCreatedAt(new Date());
        currentAccountDTO.setOverDraft(saveCurrentAccountDTO.getOverDraft());
        currentAccountDTO.setCustomerDTO(customerMapper.fromCustomer(customer));
        CurrentAccount currentAccount=bankAccountMapper.fromCurrentAccountDTO(currentAccountDTO);
        CurrentAccount currentAccount1=bankAccountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentAccount(currentAccount1);
    }

    @Override
    public AccountHistoryDTO getAccountHistory(Long bankAccountId, int page, int size) throws BankAccountNotFoundExcetion {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundExcetion("Bank Account Not Found");
        }
        // Pas besoin de cast vers Pageable ici
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccount_Id(bankAccountId, PageRequest.of(page, size));
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream()
                .map(op -> accountOperationMapper.fromAccountOperation(op))
                .collect(Collectors.toList());

        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setSize(size);
        accountHistoryDTO.setId(bankAccount.getId());
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);

        return accountHistoryDTO;
    }

    @Override
    public List<BankAccountDTO> findBankAccountByCustomerId(Long customerId) {
        List<BankAccount>  bankAccountList=bankAccountRepository.findBankAccountByCustomerId(customerId);
        List<BankAccountDTO> bankAccountDTOList= bankAccountList.stream().map(bankAccount -> bankAccountMapper.fromBankAccount(bankAccount))
                .collect(Collectors.toList());
        return bankAccountDTOList;
    }

}

