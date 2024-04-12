package com.smartgrid.SmartGrid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EntityScan(basePackages = "com.smartgrid.SmartGrid.entities")
@ComponentScan(basePackages = "com.smartgrid.SmartGrid.*")
@Slf4j
public class SmartGridApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartGridApplication.class, args);
	}

	private SocketIOServer server;
 @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0"); // Set your server hostname
        config.setPort(9092); // Set your desired port

        this.server =  new SocketIOServer(config);
		server.start();
		server.addConnectListener(new ConnectListener() {
			@Override
			public void onConnect(SocketIOClient client) {

				log.info("new user connected with socket " + client.getSessionId());
			}
		});

		server.addDisconnectListener(new DisconnectListener() {
			@Override
			public void onDisconnect(SocketIOClient client) {
				client.getNamespace().getAllClients().stream().forEach(data-> {
					log.info("user disconnected "+data.getSessionId().toString());});
			}
		});
		return server;
    }
	@PreDestroy
	public void stopSocketIOServer() {
		this.server.stop();
	}
}
