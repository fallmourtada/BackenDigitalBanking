package com.example.digitalbankingproject.service.CustomerService;

import com.example.digitalbankingproject.dto.CustomerDTO;
import com.example.digitalbankingproject.entite.Customer;
import com.example.digitalbankingproject.mapper.CustomerMapper;
import com.example.digitalbankingproject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService{
    private CustomerRepository customerRepository;

    private CustomerMapper customerMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
       Customer customer= customerMapper.fromCustomerDTO(customerDTO);
       Customer customer1=customerRepository.save(customer);
        return customerMapper.fromCustomer(customer1);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO, Long customerId) {
        customerDTO.setId(customerId);
        Customer customer=customerRepository.save(customerMapper.fromCustomerDTO(customerDTO));
        return customerMapper.fromCustomer(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
        log.info("Customer Deleted Successfully");
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        Customer customer=customerRepository.findById(customerId).get();
        CustomerDTO customerDTO=customerMapper.fromCustomer(customer);
        return customerDTO;
    }

    @Override
    public List<CustomerDTO> getListCustomers() {
        List<Customer> customerList=customerRepository.findAll();
        List<CustomerDTO> customerDTOList=customerList.stream().map(customer -> customerMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDTOList;
    }
}
