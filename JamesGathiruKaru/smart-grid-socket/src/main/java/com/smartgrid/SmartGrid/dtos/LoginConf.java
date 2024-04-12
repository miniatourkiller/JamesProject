package com.smartgrid.SmartGrid.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginConf {
    private String username;
    private String token;
    private String refreshtoken;
}
