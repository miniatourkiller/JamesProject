package com.smartgrid.SmartGrid.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class MyTime {
    public String currentDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateTimeFormatter.format(localDateTime);
    }
    public String timeStamp(){
        return ""+ new Date(System.currentTimeMillis());
    }
}
