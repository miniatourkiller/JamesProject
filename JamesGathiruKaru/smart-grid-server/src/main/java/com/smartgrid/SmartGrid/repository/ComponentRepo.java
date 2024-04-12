package com.smartgrid.SmartGrid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartgrid.SmartGrid.entities.Component;

public interface ComponentRepo extends JpaRepository<Component, Integer>{
    
}
