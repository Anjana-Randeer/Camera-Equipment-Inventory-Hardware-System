package com.camrent.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @Column(length = 20)
    private String customerId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String nicNo;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String contactNo;

    @Column(nullable = false)
    private LocalDate registeredDate;
}
