package com.example.digitalbankingproject.service.CustomerService;

import com.example.digitalbankingproject.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO,Long customerId);

    void deleteCustomer(Long customerId);

    CustomerDTO getCustomer(Long customerId);

    List<CustomerDTO> getListCustomers();
}
