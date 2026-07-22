package com.camrent.dto;

import com.camrent.entity.EquipmentType;
import com.camrent.entity.AccessoryType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EquipmentDto {
    private String equipmentId;
    private String equipmentName;
    private String brand;
    private BigDecimal purchasePrice;
    private BigDecimal rentalPerDay;
    private EquipmentType equipmentType;
    
    // Subclass specific fields
    private String model; // CameraBody
    private String zoomRange; // CameraLens
    private String mountType; // CameraLens
    private AccessoryType accessoryType; // Accessories
}
