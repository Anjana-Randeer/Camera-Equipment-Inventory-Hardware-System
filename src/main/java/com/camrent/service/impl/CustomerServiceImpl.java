package com.camrent.service.impl;

import com.camrent.entity.Customer;
import com.camrent.repository.CustomerRepository;
import com.camrent.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer registerCustomer(Customer customer) {
        customer.setRegisteredDate(LocalDate.now());
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId).orElseThrow();
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
