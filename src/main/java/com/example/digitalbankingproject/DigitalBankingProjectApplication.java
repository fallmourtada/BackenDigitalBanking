package com.example.digitalbankingproject;

import com.example.digitalbankingproject.entite.AccountOperation;
import com.example.digitalbankingproject.entite.CurrentAccount;
import com.example.digitalbankingproject.entite.Customer;
import com.example.digitalbankingproject.entite.SavingAccount;
import com.example.digitalbankingproject.enumeration.AccountSatus;
import com.example.digitalbankingproject.enumeration.OperationType;
import com.example.digitalbankingproject.repository.AccountOperationRepository;
import com.example.digitalbankingproject.repository.BankAccountRepository;
import com.example.digitalbankingproject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
@AllArgsConstructor
public class DigitalBankingProjectApplication {
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingProjectApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager){
		//PasswordEncoder passwordEncoder=passwordEncoder();
		return args -> {
			UserDetails u1=jdbcUserDetailsManager.loadUserByUsername("user1");
			if(u1==null)
			jdbcUserDetailsManager.createUser(
					User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build()
			);
			UserDetails u2=jdbcUserDetailsManager.loadUserByUsername("admin");
			if(u2==null)
			jdbcUserDetailsManager.createUser(
					User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("ADMIN").build()
			);



		};
	}

//	@Bean
//	CommandLineRunner start(CustomerRepository customerRepository,
//							BankAccountRepository bankAccountRepository,
//							AccountOperationRepository accountOperationRepository){
//		return args -> {
//
//			Stream.of("fallou","khadim","cheikh").forEach(name->{
//				Customer customer=new Customer();
//				customer.setName(name);
//				customer.setEmail(name+"@gmail.com");
//				customerRepository.save(customer);
//			});
//
//			customerRepository.findAll().forEach(cust->{
//				CurrentAccount currentAccount=new CurrentAccount();
//				currentAccount.setCustomer(cust);
//				currentAccount.setOverDraft(9000);
//				currentAccount.setBalance(Math.random()*9000);
//				currentAccount.setCreatedAt(new Date());
//				bankAccountRepository.save(currentAccount);
//			});
//
//			customerRepository.findAll().forEach(cust->{
//				SavingAccount savingAccount=new SavingAccount();
//				savingAccount.setCustomer(cust);
//				savingAccount.setBalance(Math.random()*2000);
//				savingAccount.setBalance(10000);
//				savingAccount.setCreatedAt(new Date());
//				savingAccount.setStatus(AccountSatus.CREATED);
//				savingAccount.setInterestRate(5.5);
//				bankAccountRepository.save(savingAccount);
//			});
//
//			bankAccountRepository.findAll().forEach(bankAccount -> {
//				for(int i=0; i<10; i++){
//					AccountOperation accountOperation=new AccountOperation();
//					accountOperation.setBankAccount(bankAccount);
//					accountOperation.setOperationDate(new Date());
//					accountOperation.setAmount(Math.random()*12000);
//					accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
//					accountOperationRepository.save(accountOperation);
//				}
//
//
//			});
//
//		};
//	}



//	@Bean
//	public PasswordEncoder passwordEncoder(){
//		return new BCryptPasswordEncoder();
//	}
}
