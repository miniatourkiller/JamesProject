package com.example.smartgrid.dto;

import java.util.ArrayList;

public class Component {
    private int id;
    private String name;
    private double usageValue;

   ArrayList<Component> components;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
