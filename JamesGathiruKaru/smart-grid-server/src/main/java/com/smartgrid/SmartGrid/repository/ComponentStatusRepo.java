package com.smartgrid.SmartGrid.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartgrid.SmartGrid.entities.ComponentStatus;

public interface ComponentStatusRepo extends JpaRepository<ComponentStatus, Integer>{
    @Query("FROM ComponentStatus cs WHERE cs.username = ?1 AND cs.componentName = ?2")
    public ComponentStatus findByUsernameAndComponentName(String username, String componentName);
    @Query("FROM ComponentStatus cs WHERE cs.username = ?1")
    public ArrayList<ComponentStatus> findAllByUserName(String username);
}
