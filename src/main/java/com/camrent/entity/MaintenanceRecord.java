package com.camrent.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maintenance_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecord {

    @Id
    @Column(length = 20)
    private String recordId;

    @ManyToOne
    @JoinColumn(name = "equipmentId", nullable = false)
    private Equipment equipment;

    @Column(nullable = false)
    private LocalDate serviceDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal cost;

    @Column(length = 255)
    private String issueDescription;
}
