package com.example.smartgrid.dto;

import java.util.ArrayList;
import java.util.Collection;

public class Record {
    private int id;
    private String username;
    private String date;
    private String timeStamp;
    private ArrayList<Component> components = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Collection<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }
}
