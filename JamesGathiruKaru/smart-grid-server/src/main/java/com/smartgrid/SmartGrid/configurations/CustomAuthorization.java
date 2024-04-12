package com.smartgrid.SmartGrid.configurations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.json.JsonMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorization extends OncePerRequestFilter{
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if(request.getServletPath().equals("/login") || request.getServletPath().equals("/adminsignup")|| request.getServletPath().equals("/units")
        || this.checkPath(request.getServletPath(), "/ard")){
            filterChain.doFilter(request, response);
        }else{
            String token = request.getHeader("Authorization");
            if(token == null){
                //return something negative
                log.warn("no token");
                this.response("fail", "no token", response);
            }else{
                if(!token.startsWith("Bearer")){
                    log.warn("NO KEY WORD");
                    //return something negative
                    this.response("fail", "NO KEY WORD", response);
                }
                token = token.substring("Bearer ".length());
                Algorithm algo = Algorithm.HMAC256("secret".getBytes());
                try{
                    JWTVerifier verifier = JWT.require(algo).build();
                    DecodedJWT decodedJWT = verifier.verify(token);
                    log.info("JWT VERIFIED");
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    for(String role: roles){
                        authorities.add(new SimpleGrantedAuthority(role));
                    }
                    UsernamePasswordAuthenticationToken token2 = new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(token2);
                    filterChain.doFilter(request, response);
                }catch(Exception e){
                    //return expiration notice
                    log.warn("token EXPIRED");
                    this.response("fail", "token expired", response);
                }
            }
        }
    }
    
    private boolean checkPath(String servlet, String path){
            if(servlet.startsWith(path)){
                return true;
            }else{
                return false;
            }
    }
}
