package com.smartgrid.SmartGrid.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;
import com.smartgrid.SmartGrid.dtos.Auth;
import com.smartgrid.SmartGrid.dtos.HistoryReq;
import com.smartgrid.SmartGrid.dtos.LoginConf;
import com.smartgrid.SmartGrid.entities.Client;
import com.smartgrid.SmartGrid.entities.ComponentStatus;
import com.smartgrid.SmartGrid.entities.Message;
import com.smartgrid.SmartGrid.entities.Records;
import com.smartgrid.SmartGrid.enums.Role;
import com.smartgrid.SmartGrid.interfaces.UserServices;
import com.smartgrid.SmartGrid.repository.ClientRepo;
import com.smartgrid.SmartGrid.repository.ComponentRepo;
import com.smartgrid.SmartGrid.repository.ComponentStatusRepo;
import com.smartgrid.SmartGrid.repository.RecordsRepo;

import io.socket.client.Socket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Implementation implements UserDetailsService, UserServices{

    @Autowired
    ClientRepo clientRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = this.getUserByUsername(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(client.getRole().toString()));
        org.springframework.security.core.userdetails.User authenticationToken = new org.springframework.security.core.userdetails.User(client.getUsername(), client.getPassword(),authorities);
        return authenticationToken;
    }

    @Override
    public Client signup(Auth auth, HttpServletResponse response) {
        Client client = clientRepo.findByUsername(auth.getUsername());
        if(client != null){
            log.warn("THE CLIENT :"+auth.getUsername()+" EXISTS");
            this.response("fail", "user exists", response);
            return null;
        }else{
            log.info("ADDING NEW USER: "+ auth.getUsername());
            client = new Client();
            client.setUsername(auth.getUsername());
            client.setPassword(encoder.encode(auth.getPassword()));
            client.setRole(Role.CLIENT);
            clientRepo.save(client);
            return client;
        }
    }

    @Override
    public Client getUserByUsername(String username) {
        return clientRepo.findByUsername(username);
    }

    ///response out of context

    JsonMapper jsonMapper = new JsonMapper();
    public void response(String status, String message, HttpServletResponse response){
        response.setContentType("application/json");
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        try{
            jsonMapper.writeValue(response.getOutputStream(), map);
        }catch(Exception e){
            log.warn(e.getMessage());
            log.warn("Response output failure");
        }
    }


    ///getting username from the security context holder

    @Override
    public String username(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("SECURITY CONTEXT HOLDER NAME: "+ username);
        return username;
    }


    @Autowired
    PasswordEncoder encoder;

    @Override
    public Client adminsignup(Auth auth, HttpServletResponse response) {
       Client client = new Client();
       if(this.getUserByUsername(auth.getUsername()) != null){
        this.response("fail", "User already exists", response);
        return null;
       }else{
        client.setUsername(auth.getUsername());
        client.setPassword(encoder.encode(auth.getPassword()));
        try{
            client.setRole(Role.valueOf(auth.getRole()));
        }catch(Exception e){
            log.warn("No such role as");
            log.warn(auth.getRole());
            log.info("Will save with default role: ADMIN");
            client.setRole(Role.ADMIN);
        }
        clientRepo.save(client);
        return client;
        
       }
    }

    @Override
    public LoginConf login(Auth auth, HttpServletResponse response) {
        Client client = this.getUserByUsername(auth.getUsername()) ;
        if(client == null){
            this.response("fail", "wrong credentials", response);
            return null;
        }else{
            if(encoder.matches(auth.getPassword(), client.getPassword())){
                Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(client.getRole().toString()));
                User user = new User(client.getUsername(), client.getPassword(), authorities);
                LoginConf loginConf = LoginConf.builder()
                .username(auth.getUsername())
                .token(this.createAccessToken(user))
                .refreshtoken(this.createRefreshToken(user))
                .build();
                return loginConf;
            }else{
                this.response("fail", "wrong credentials", response);
                log.warn(auth.getUsername());
                log.warn("ATTEMPTED LOGIN");
            return null;
            }
        }
    }

    Algorithm algo = Algorithm.HMAC256("secret".getBytes());
	public String createAccessToken(User user) {
		
		return JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+24*60*60*1000))
				.withIssuer("smart Grid")
				.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.sign(algo);
	
	}
	public String createRefreshToken(User user) {
		return  JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000))
				.withIssuer("smart grid")
				.sign(algo);
	}

    @Override
    public LoginConf refreshtoken(  HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("Authorization");
        if(token == null){
            log.warn("NO HEADER");
            this.response("fail", "no Header provided", response);
            return null;
        }
        if(!token.startsWith("Refresh")){
            log.warn("NO KEY WORD PROVIDED");
            this.response("fail", "No key word", response);
            return null;
        }
        token = token.substring("Refresh ".length());
        try{
            JWTVerifier verifier = JWT.require(algo).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            log.info("NEW TOKENS ISSUED TO: ");
            log.info(decodedJWT.getSubject());
            String subject = decodedJWT.getSubject();
            Client client = this.getUserByUsername(subject);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(client.getRole().toString()));
            User user = new User(client.getUsername(), client.getPassword(), authorities);
            LoginConf loginConf = LoginConf
                                    .builder()
                                    .token(this.createAccessToken(user))
                                    .refreshtoken(this.createRefreshToken(user))
                                    .username(subject)
                                    .build();
            return loginConf;
        }catch(Exception e)
        {
            log.warn(e.getLocalizedMessage());
            this.response("fail", e.getMessage(), response);
            return null;
        }
    }

    
    Gson gson = new Gson();

    @Autowired
    ComponentStatusRepo componentStatusRepo;
    @Override
    public ComponentStatus addOrUpdateComponentStatus(ComponentStatus componentStatus){
        ComponentStatus componentStatus2 = componentStatusRepo.findByUsernameAndComponentName(this.username(), componentStatus.getComponentName());
        Message message = new Message();
        message.setMessage("hello there");
        if(componentStatus2 == null){
            componentStatus.setUsername(this.username());
            componentStatus.setStatus(false);
            log.info("Added a new Component with a status");
            componentStatusRepo.save(componentStatus);
            message.setFrom(componentStatus.getUsername()+"status");
            socket.emit("fromClient2", gson.toJson(message));
            log.info("attempted sending mseesage");
            return componentStatus;
        }else{
            componentStatus2.setStatus(!componentStatus2.isStatus());
            componentStatusRepo.save(componentStatus2);
            message.setFrom(componentStatus2.getUsername()+"status");
            socket.emit("fromClient2", gson.toJson(message));
            log.info("attempted sending mseesage");
            log.info("UPDATED STATUS FOR "+componentStatus2.getUsername()+"'s "+componentStatus2.getComponentName());
            return componentStatus2;
        }
    }

    @Override
    public void removeComponent(ComponentStatus componentStatus, HttpServletResponse response){
        ComponentStatus componentStatus2 = componentStatusRepo.findByUsernameAndComponentName(this.username(), componentStatus.getComponentName());
        Message message = new Message();
        message.setMessage("hello there");
        if(componentStatus2 != null){
            componentStatusRepo.delete(componentStatus2);
        }
        message.setFrom(componentStatus2.getUsername()+"status");
        socket.emit("fromClient2", gson.toJson(message));
        log.info("attempted sending mseesage");
        this.response("success", "Component Removed", response);
    }

    @Autowired
    RecordsRepo recordsRepo;
    @Autowired
    ComponentRepo componentRepo;
    @Autowired
    MyTime myTime;

    @Autowired
    Socket socket;



    @Override
    public void unitsReading(Records records){
        log.info("Units reading captured. Its time stamp: " + records.getTimeStamp());
        componentRepo.saveAll(records.getComponents());
        records.setDate(myTime.currentDate());
        records.setTimeStamp(myTime.timeStamp());
        recordsRepo.save(records);
        Message message = new Message();

        message.setFrom(records.getUsername());
        message.setRecords(records);
        socket.emit("records", gson.toJson(message));

        log.info("Record saved");
    }

    @Override
    public boolean getComponentStatus(String componentName){
        ComponentStatus componentStatus = componentStatusRepo.findByUsernameAndComponentName(this.username(), componentName);
        if(componentStatus != null){
            log.info("COMPONENT FOUND AND NOW CHECKING STATUS .....");
            return componentStatus.isStatus();
        }
        log.warn("COMPONENT NOT FOUND HENCE GIVING DEFAULT BOOLEAN: FALSE");
        return false;
    }

    @Override
    public ArrayList<Records> getHistory(HistoryReq historyReq){
        log.info("REQUESTING HISTORY INFO");
        log.info("REQUESTED DATE: "+ historyReq.getDate());
        return recordsRepo.findRecordsByUsernameAndDate(this.username(), historyReq.getDate());
    }

    @Override
    public ArrayList<ComponentStatus> getComponets() {
        log.info("trying to access components");
        return componentStatusRepo.findAllByUserName(this.username());
    }

    @Override
    public ArrayList<ComponentStatus> ardGetComponets(String username) {
        log.info("trying to access components");
        return componentStatusRepo.findAllByUserName(username);
    }
    
}
