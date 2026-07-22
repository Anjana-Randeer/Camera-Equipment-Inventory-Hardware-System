package com.camrent.config;

import com.camrent.entity.*;
import com.camrent.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final EquipmentRepository equipmentRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // We wipe the existing equipment data securely using native queries to bypass foreign key constraint loops
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        jdbcTemplate.execute("TRUNCATE TABLE equipment");
        jdbcTemplate.execute("TRUNCATE TABLE camera_body");
        jdbcTemplate.execute("TRUNCATE TABLE camera_lens");
        jdbcTemplate.execute("TRUNCATE TABLE accessories");
        jdbcTemplate.execute("TRUNCATE TABLE rental_transaction_equipment");
        jdbcTemplate.execute("TRUNCATE TABLE maintenance_record");
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");

        // 1. Cameras
        seedCamera("CAM001", "Sony A7 iii", "Sony", new BigDecimal("4500.00"));
        seedCamera("CAM002", "Sony A7 iv", "Sony", new BigDecimal("6000.00"));
        seedCamera("CAM003", "Sony A7 v", "Sony", new BigDecimal("8000.00"));
        seedCamera("CAM004", "Canon 6D", "Canon", new BigDecimal("2000.00"));
        seedCamera("CAM005", "Canon 6D mark II", "Canon", new BigDecimal("3500.00"));
        seedCamera("CAM006", "Canon R", "Canon", new BigDecimal("4000.00"));
        seedCamera("CAM007", "Nikon D850", "Nikon", new BigDecimal("4000.00"));
        seedCamera("CAM008", "Nikon D7500", "Nikon", new BigDecimal("3500.00"));
        seedCamera("CAM009", "Nikon Z6", "Nikon", new BigDecimal("6000.00"));

        // 2. Lenses
        seedLens("LEN001", "Sony 28-70 mm F3.5", "Sony", new BigDecimal("1500.00"));
        seedLens("LEN002", "Sony 70-200 mm F2.8", "Sony", new BigDecimal("5000.00"));
        seedLens("LEN003", "Sony 50 mm F1.8", "Sony", new BigDecimal("1500.00"));
        seedLens("LEN004", "Canon Sigma 35 mm F1.4", "Canon", new BigDecimal("2500.00"));
        seedLens("LEN005", "Canon 24-105 mm F4", "Canon", new BigDecimal("1500.00"));
        seedLens("LEN006", "Canon 50 mm F1.8", "Canon", new BigDecimal("750.00"));
        seedLens("LEN007", "Nikon 50 mm F1.8", "Nikon", new BigDecimal("1000.00"));
        seedLens("LEN008", "Nikon 70-200 mm F2.8", "Nikon", new BigDecimal("3000.00"));
        seedLens("LEN009", "Nikon Z 50 mm F1.4", "Nikon", new BigDecimal("2500.00"));

        // 3. Accessories
        // Flash light/Video light
        seedAccessory("ACC001", "Godox V1", "Godox", new BigDecimal("1000.00"), AccessoryType.FLASHLIGHT);
        seedAccessory("ACC002", "AD600 full set", "Godox", new BigDecimal("3500.00"), AccessoryType.FLASHLIGHT);
        seedAccessory("ACC003", "Video light 500K(with battery)", "Generic", new BigDecimal("1500.00"), AccessoryType.FLASHLIGHT);
        seedAccessory("ACC004", "Godox LC 500R RGB light", "Godox", new BigDecimal("2000.00"), AccessoryType.FLASHLIGHT);
        seedAccessory("ACC005", "Nantube light", "Nanlite", new BigDecimal("2000.00"), AccessoryType.FLASHLIGHT);
        seedAccessory("ACC006", "Canon Trigger X2", "Canon", new BigDecimal("500.00"), AccessoryType.FLASHLIGHT);
        seedAccessory("ACC007", "Nikon Trigger X2", "Nikon", new BigDecimal("500.00"), AccessoryType.FLASHLIGHT);
        seedAccessory("ACC008", "Snoot", "Generic", new BigDecimal("500.00"), AccessoryType.FLASHLIGHT);

        // Gimbal Stabilizer
        seedAccessory("ACC009", "Osmo Mobile 7", "DJI", new BigDecimal("2000.00"), AccessoryType.GIMBAL_STABILIZER);
        seedAccessory("ACC010", "Ronin RS4 Gimbal", "DJI", new BigDecimal("4000.00"), AccessoryType.GIMBAL_STABILIZER);
        seedAccessory("ACC011", "DJI Osmo Pocket 3", "DJI", new BigDecimal("2500.00"), AccessoryType.GIMBAL_STABILIZER);
        seedAccessory("ACC012", "Photo Tripod", "Generic", new BigDecimal("1000.00"), AccessoryType.TRIPOD);
        seedAccessory("ACC013", "Video Tripod", "Generic", new BigDecimal("1000.00"), AccessoryType.TRIPOD);
        seedAccessory("ACC014", "Monopod", "Generic", new BigDecimal("500.00"), AccessoryType.TRIPOD);

        // MIC
        seedAccessory("ACC015", "RODE GO wireless Mic", "RODE", new BigDecimal("2500.00"), AccessoryType.MIC);
        seedAccessory("ACC016", "RODE GO II Pro", "RODE", new BigDecimal("3000.00"), AccessoryType.MIC);
        seedAccessory("ACC017", "DJI MIC", "DJI", new BigDecimal("2500.00"), AccessoryType.MIC);
        seedAccessory("ACC018", "BOOM MIC", "Generic", new BigDecimal("3000.00"), AccessoryType.MIC);
        seedAccessory("ACC019", "PODCAST MIC with Arm", "Generic", new BigDecimal("3000.00"), AccessoryType.MIC);
        seedAccessory("ACC020", "MIXER", "Generic", new BigDecimal("3000.00"), AccessoryType.MIC);
    }

    private void seedCamera(String id, String name, String brand, BigDecimal price) {
        CameraBody camera = new CameraBody();
        camera.setEquipmentId(id);
        camera.setEquipmentName(name);
        camera.setBrand(brand);
        camera.setPurchasePrice(new BigDecimal("100000.00")); // Dummy purchase price
        camera.setRentalPerDay(price);
        camera.setEquipmentType(EquipmentType.CAMERA_BODY);
        camera.setEquipmentStatus(EquipmentStatus.AVAILABLE);
        camera.setModel(name);
        equipmentRepository.save(camera);
    }

    private void seedLens(String id, String name, String brand, BigDecimal price) {
        CameraLens lens = new CameraLens();
        lens.setEquipmentId(id);
        lens.setEquipmentName(name);
        lens.setBrand(brand);
        lens.setPurchasePrice(new BigDecimal("50000.00")); // Dummy purchase price
        lens.setRentalPerDay(price);
        lens.setEquipmentType(EquipmentType.CAMERA_LENS);
        lens.setEquipmentStatus(EquipmentStatus.AVAILABLE);
        lens.setZoomRange(name);
        equipmentRepository.save(lens);
    }

    private void seedAccessory(String id, String name, String brand, BigDecimal price, AccessoryType type) {
        Accessories accessory = new Accessories();
        accessory.setEquipmentId(id);
        accessory.setEquipmentName(name);
        accessory.setBrand(brand);
        accessory.setPurchasePrice(new BigDecimal("25000.00")); // Dummy purchase price
        accessory.setRentalPerDay(price);
        accessory.setEquipmentType(EquipmentType.ACCESSORY);
        accessory.setEquipmentStatus(EquipmentStatus.AVAILABLE);
        accessory.setAccessoryType(type);
        equipmentRepository.save(accessory);
    }
}
