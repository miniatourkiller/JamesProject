package com.smartgrid.SmartGrid;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.socket.client.IO;
import io.socket.client.Socket;

@SpringBootApplication
@EntityScan(basePackages = "com.smartgrid.SmartGrid.entities")
@ComponentScan(basePackages = "com.smartgrid.SmartGrid.*")
@EnableJpaRepositories(basePackages = "com.smartgrid.SmartGrid.repository")
public class SmartGridApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartGridApplication.class, args);
	}
	@Bean
	PasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}

    private Socket socket;
   @Bean
    public Socket socket() throws URISyntaxException{
    //    return IO.socket("https://smartgridsocket.onrender.com");

        this.socket = IO.socket(URI.create("https://smartgridsocket.onrender.com"));
        socket.connect();
        return socket;
   
    }
	
}
