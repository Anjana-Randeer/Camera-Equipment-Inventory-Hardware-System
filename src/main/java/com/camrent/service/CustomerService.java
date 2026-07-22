package com.camrent.service;

import com.camrent.entity.Customer;
import java.util.List;

public interface CustomerService {
    Customer registerCustomer(Customer customer);
    Customer getCustomerById(String customerId);
    List<Customer> getAllCustomers();
}
