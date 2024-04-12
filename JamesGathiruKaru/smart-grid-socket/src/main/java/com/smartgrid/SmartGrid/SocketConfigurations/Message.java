package com.smartgrid.SmartGrid.SocketConfigurations;

import lombok.Data;

@Data
public class Message {
    private String from;
    private String message;
    private Records records;
}
