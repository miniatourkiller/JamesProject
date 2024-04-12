package com.smartgrid.SmartGrid.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.smartgrid.SmartGrid.entities.Records;

public interface RecordsRepo extends JpaRepository<Records, Integer>{
    @Query("FROM Records r WHERE r.username = ?1 AND r.date = ?2")
    public ArrayList<Records> findRecordsByUsernameAndDate(String username, String date);
}
