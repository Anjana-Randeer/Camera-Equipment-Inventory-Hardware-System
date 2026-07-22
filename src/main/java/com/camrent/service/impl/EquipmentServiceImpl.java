package com.camrent.service.impl;

import com.camrent.entity.*;
import com.camrent.dto.EquipmentDto;
import com.camrent.repository.EquipmentRepository;
import com.camrent.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Override
    public Equipment addEquipment(EquipmentDto dto) {
        Equipment equipment;
        switch (dto.getEquipmentType()) {
            case CAMERA_BODY:
                CameraBody body = new CameraBody();
                body.setModel(dto.getModel());
                equipment = body;
                break;
            case CAMERA_LENS:
                CameraLens lens = new CameraLens();
                lens.setZoomRange(dto.getZoomRange());
                lens.setMountType(dto.getMountType());
                equipment = lens;
                break;
            case ACCESSORY:
                Accessories acc = new Accessories();
                acc.setAccessoryType(dto.getAccessoryType());
                equipment = acc;
                break;
            default:
                throw new IllegalArgumentException("Unknown equipment type");
        }
        equipment.setEquipmentId(dto.getEquipmentId());
        equipment.setEquipmentName(dto.getEquipmentName());
        equipment.setBrand(dto.getBrand());
        equipment.setPurchasePrice(dto.getPurchasePrice());
        equipment.setRentalPerDay(dto.getRentalPerDay());
        equipment.setEquipmentStatus(EquipmentStatus.AVAILABLE);
        equipment.setEquipmentType(dto.getEquipmentType());
        
        return equipmentRepository.save(equipment);
    }

    @Override
    public void removeEquipment(String equipmentId) {
        equipmentRepository.deleteById(equipmentId);
    }

    @Override
    public Equipment updateStatus(String equipmentId, EquipmentStatus status) {
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow();
        equipment.setEquipmentStatus(status);
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateRentalPrice(String equipmentId, BigDecimal newPrice) {
        Equipment equipment = equipmentRepository.findById(equipmentId).orElseThrow();
        equipment.setRentalPerDay(newPrice);
        return equipmentRepository.save(equipment);
    }

    @Override
    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    @Override
    public List<Equipment> getEquipmentByType(EquipmentType type) {
        return equipmentRepository.findAll().stream()
            .filter(e -> e.getEquipmentType() == type)
            .toList();
    }

    @Override
    public List<Equipment> getAvailableEquipmentByType(EquipmentType type) {
        return equipmentRepository.findByEquipmentTypeAndEquipmentStatus(type, EquipmentStatus.AVAILABLE);
    }
}
