package com.camrent.repository;

import com.camrent.entity.RentalTransaction;
import com.camrent.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalTransactionRepository extends JpaRepository<RentalTransaction, String> {
    List<RentalTransaction> findByCustomer(Customer customer);
    List<RentalTransaction> findByCustomerAndReturnDateIsNull(Customer customer);
}
