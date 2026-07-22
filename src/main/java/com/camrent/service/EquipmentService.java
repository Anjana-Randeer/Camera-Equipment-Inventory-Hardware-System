package com.camrent.service;

import com.camrent.entity.Equipment;
import com.camrent.entity.EquipmentStatus;
import com.camrent.entity.EquipmentType;
import com.camrent.dto.EquipmentDto;
import java.math.BigDecimal;
import java.util.List;

public interface EquipmentService {
    Equipment addEquipment(EquipmentDto equipmentDto);
    void removeEquipment(String equipmentId);
    Equipment updateStatus(String equipmentId, EquipmentStatus status);
    Equipment updateRentalPrice(String equipmentId, BigDecimal newPrice);
    
    List<Equipment> getAllEquipment();
    List<Equipment> getEquipmentByType(EquipmentType type);
    List<Equipment> getAvailableEquipmentByType(EquipmentType type);
}
