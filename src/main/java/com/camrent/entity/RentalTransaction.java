package com.camrent.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "rental_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalTransaction {

    @Id
    @Column(length = 20)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "staffId", nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private LocalDate checkoutDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal lateFee = BigDecimal.ZERO;

    @ManyToMany
    @JoinTable(
        name = "rental_transaction_equipment",
        joinColumns = @JoinColumn(name = "transactionId"),
        inverseJoinColumns = @JoinColumn(name = "equipmentId")
    )
    private List<Equipment> equipments;
}
