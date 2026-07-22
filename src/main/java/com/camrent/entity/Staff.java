package com.camrent.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Staff extends User {
}
