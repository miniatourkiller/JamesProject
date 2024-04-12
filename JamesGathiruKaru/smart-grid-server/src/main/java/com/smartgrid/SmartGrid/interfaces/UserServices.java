package com.smartgrid.SmartGrid.interfaces;

import java.util.ArrayList;

import com.smartgrid.SmartGrid.dtos.Auth;
import com.smartgrid.SmartGrid.dtos.HistoryReq;
import com.smartgrid.SmartGrid.dtos.LoginConf;
import com.smartgrid.SmartGrid.entities.Client;
import com.smartgrid.SmartGrid.entities.ComponentStatus;
import com.smartgrid.SmartGrid.entities.Records;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserServices {
    public Client signup(Auth auth, HttpServletResponse response);
    public Client getUserByUsername(String username);
    public Client adminsignup(Auth auth, HttpServletResponse response);
    public LoginConf login(Auth auth, HttpServletResponse response);
    public LoginConf refreshtoken(HttpServletRequest request, HttpServletResponse  response);
    public String username();
    public ComponentStatus addOrUpdateComponentStatus(ComponentStatus componentStatus);
    public void removeComponent(ComponentStatus componentStatus, HttpServletResponse response);
    public void unitsReading(Records records);
    public boolean getComponentStatus(String componentName);
    public ArrayList<Records> getHistory(HistoryReq historyReq);
    public ArrayList<ComponentStatus> getComponets();
    public ArrayList<ComponentStatus> ardGetComponets(String username);
}
