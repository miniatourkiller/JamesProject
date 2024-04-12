package com.smartgrid.SmartGrid.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Entity
@Data
public class Component {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private float usageValue;
}
