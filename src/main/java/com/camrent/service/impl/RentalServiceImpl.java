package com.camrent.service.impl;

import com.camrent.entity.*;
import com.camrent.repository.*;
import com.camrent.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {

    private final RentalTransactionRepository rentalRepository;
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final EquipmentRepository equipmentRepository;
    private final PaymentRepository paymentRepository;
    private final MaintenanceRecordRepository maintenanceRepository;

    @Override
    @Transactional
    public RentalTransaction checkout(String staffId, String customerId, List<String> equipmentIds, int days) {
        Staff staff = staffRepository.findById(staffId).orElseThrow();
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        
        List<Equipment> equipments = equipmentIds.stream()
            .map(id -> equipmentRepository.findById(id).orElseThrow())
            .peek(e -> {
                if (e.getEquipmentStatus() != EquipmentStatus.AVAILABLE) {
                    throw new RuntimeException("Equipment " + e.getEquipmentId() + " is not available");
                }
                e.setEquipmentStatus(EquipmentStatus.RENTED);
                equipmentRepository.save(e);
            })
            .collect(Collectors.toList());

        RentalTransaction transaction = new RentalTransaction();
        transaction.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        transaction.setStaff(staff);
        transaction.setCustomer(customer);
        transaction.setCheckoutDate(LocalDate.now());
        transaction.setDueDate(LocalDate.now().plusDays(days));
        transaction.setEquipments(equipments);

        return rentalRepository.save(transaction);
    }

    @Override
    @Transactional
    public RentalTransaction checkIn(String transactionId, boolean isDamaged, String damageDescription, BigDecimal damageCost, String paymentMethod) {
        RentalTransaction transaction = rentalRepository.findById(transactionId).orElseThrow();
        transaction.setReturnDate(LocalDate.now());
        
        long daysLate = ChronoUnit.DAYS.between(transaction.getDueDate(), transaction.getReturnDate());
        if (daysLate > 0) {
            BigDecimal lateFeePerDay = new BigDecimal("500.00");
            transaction.setLateFee(lateFeePerDay.multiply(new BigDecimal(daysLate)));
        }

        long rentalDays = ChronoUnit.DAYS.between(transaction.getCheckoutDate(), transaction.getDueDate());
        if (rentalDays == 0) rentalDays = 1;
        
        final long finalRentalDays = rentalDays;
        BigDecimal totalRentalCost = transaction.getEquipments().stream()
                .map(e -> e.getRentalPerDay().multiply(new BigDecimal(finalRentalDays)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal finalAmount = totalRentalCost.add(transaction.getLateFee());

        Payment payment = new Payment();
        payment.setPaymentId("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        payment.setRentalTransaction(transaction);
        payment.setAmount(finalAmount);
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(paymentMethod);
        paymentRepository.save(payment);

        for (Equipment equipment : transaction.getEquipments()) {
            if (isDamaged) {
                equipment.setEquipmentStatus(EquipmentStatus.MAINTENANCE);
                
                MaintenanceRecord record = new MaintenanceRecord();
                record.setRecordId("MR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                record.setEquipment(equipment);
                record.setServiceDate(LocalDate.now());
                record.setIssueDescription(damageDescription);
                record.setCost(damageCost);
                maintenanceRepository.save(record);
            } else {
                equipment.setEquipmentStatus(EquipmentStatus.AVAILABLE);
            }
            equipmentRepository.save(equipment);
        }

        return rentalRepository.save(transaction);
    }

    @Override
    public List<RentalTransaction> getActiveRentalsByCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow();
        return rentalRepository.findByCustomerAndReturnDateIsNull(customer);
    }
}
