package com.ql.appquanly.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "vehicle", uniqueConstraints = { @UniqueConstraint(columnNames = "bien_so") })
@Data
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hang_xe")
    private String hang_xe;

    @Column(name = "bien_so", unique = true)
    private String bien_so;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    Employee employee;

}
