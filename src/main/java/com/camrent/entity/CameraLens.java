package com.camrent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "camera_lens")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CameraLens extends Equipment {
    @Column(length = 100)
    private String model;

    @Column(length = 50)
    private String zoomRange;

    @Column(length = 20)
    private String aperture;

    @Column(length = 50)
    private String mountType;
}
