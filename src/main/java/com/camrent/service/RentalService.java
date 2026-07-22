package com.camrent.service;

import com.camrent.entity.RentalTransaction;
import java.util.List;
import java.math.BigDecimal;

public interface RentalService {
    RentalTransaction checkout(String staffId, String customerId, List<String> equipmentIds, int days);
    RentalTransaction checkIn(String transactionId, boolean isDamaged, String damageDescription, BigDecimal damageCost, String paymentMethod);
    List<RentalTransaction> getActiveRentalsByCustomer(String customerId);
}
