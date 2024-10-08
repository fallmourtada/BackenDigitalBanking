package com.example.digitalbankingproject.controller;

import com.example.digitalbankingproject.dto.CustomerDTO;
import com.example.digitalbankingproject.service.CustomerService.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class CustomerController {
    private CustomerService customerService;

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return customerService.saveCustomer(customerDTO);
    }

    @GetMapping("/customers")
    public List<CustomerDTO> getListCustomers(){
        return customerService.getListCustomers();
    }

    @DeleteMapping("/customers/{customerId}")
    public  void deleteCustomers(@PathVariable Long customerId){
        customerService.deleteCustomer(customerId);

    }

    @GetMapping("/customers/{customerId}")
    public CustomerDTO getCustomer(@PathVariable Long customerId){
        return customerService.getCustomer(customerId);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO UpdateCustomer(@RequestBody CustomerDTO customerDTO,@PathVariable Long customerId){
        return customerService.updateCustomer(customerDTO,customerId);

    }
}
