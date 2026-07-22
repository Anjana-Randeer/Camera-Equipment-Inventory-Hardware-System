package com.camrent.repository;

import com.camrent.entity.Equipment;
import com.camrent.entity.EquipmentStatus;
import com.camrent.entity.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, String> {
    List<Equipment> findByEquipmentStatus(EquipmentStatus status);
    List<Equipment> findByEquipmentTypeAndEquipmentStatus(EquipmentType type, EquipmentStatus status);
}
