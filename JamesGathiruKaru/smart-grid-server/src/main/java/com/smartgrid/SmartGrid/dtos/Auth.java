package com.smartgrid.SmartGrid.dtos;

import lombok.Data;

@Data
public class Auth {
    private String username;
    private String password;
    private String role;
}
