package com.smartgrid.SmartGrid.SocketConfigurations;

import java.util.Collection;

import lombok.Data;

@Data
public class Records {
    private int id;
    private String username;
    private String date;
    private String timeStamp;
    private Collection<Component> components;
}
