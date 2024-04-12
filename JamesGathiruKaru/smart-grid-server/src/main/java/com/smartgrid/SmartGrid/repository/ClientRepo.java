package com.smartgrid.SmartGrid.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartgrid.SmartGrid.entities.Client;

public interface ClientRepo extends JpaRepository<Client, Integer>{
    public Client findByUsername(String username);
}
