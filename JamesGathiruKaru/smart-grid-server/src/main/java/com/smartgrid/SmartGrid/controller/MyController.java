package com.smartgrid.SmartGrid.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.smartgrid.SmartGrid.dtos.Auth;
import com.smartgrid.SmartGrid.dtos.HistoryReq;
import com.smartgrid.SmartGrid.dtos.LoginConf;
import com.smartgrid.SmartGrid.entities.Client;
import com.smartgrid.SmartGrid.entities.ComponentStatus;
import com.smartgrid.SmartGrid.entities.Records;
import com.smartgrid.SmartGrid.interfaces.UserServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class MyController {
    @GetMapping("/")
    public String hello(){
        return "hello";
    }
    @Autowired
    UserServices services;
   @PostMapping("/adminsignup")
   public Client adminsignup(@RequestBody Auth auth, HttpServletResponse response){
    return services.adminsignup(auth, response);
   }
   @PostMapping("/login")
   public LoginConf login(@RequestBody Auth auth, HttpServletResponse response){
    return services.login(auth, response);
   }
   @PostMapping("/refreshtoken")
   public LoginConf refreshToken(HttpServletResponse response, HttpServletRequest request){
    return services.refreshtoken(request, response);
   }
   @PostMapping("/signup")
   public Client signupClient(@RequestBody Auth auth, HttpServletResponse response){
        return services.signup(auth, response);
   }

   @PostMapping("/addOrUpdateComponentStatus")
   public ComponentStatus addOrUpdateComponentStatus(@RequestBody ComponentStatus componentStatus){
        return services.addOrUpdateComponentStatus(componentStatus);
   }

   @PostMapping("/removeComponent")
    public void removeComponent(@RequestBody ComponentStatus componentStatus, HttpServletResponse response){
        services.removeComponent(componentStatus, response);
    }

    @PostMapping("/units")
    public void unitsReading(@RequestBody Records records){
        services.unitsReading(records);
    }

    @GetMapping("/getComponentStatus/{componentName}")
    public boolean getComponentStatus(@PathVariable(value = "componentName") String componentName){
        return services.getComponentStatus(componentName);
    }

    @PostMapping("/getHistory")
    public ArrayList<Records> getHistory(@RequestBody HistoryReq historyReq){
        return services.getHistory(historyReq);
    }

    @GetMapping("/getComponents")
    public ArrayList<ComponentStatus> getComponentStatus(){
        return services.getComponets();
    }
    @GetMapping("/ard/getComponents/{username}")
    public ArrayList<ComponentStatus> ardgetComponentStatus(@PathVariable(value = "username") String username){
        return services.ardGetComponets(username);
    }
}
