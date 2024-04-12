package com.smartgrid.SmartGrid.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
        .csrf((csrf)->csrf.disable())
        .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.requestMatchers("/login", "/adminsignup","/refreshtoken", "/units","/ard/**").permitAll())
        .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.requestMatchers("/signup").hasAnyAuthority("ADMIN"))  
        .authorizeHttpRequests((authorizeHttpRequests)->authorizeHttpRequests.anyRequest().authenticated())
        .sessionManagement((sessionManagement)->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(new CustomAuthorization(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
