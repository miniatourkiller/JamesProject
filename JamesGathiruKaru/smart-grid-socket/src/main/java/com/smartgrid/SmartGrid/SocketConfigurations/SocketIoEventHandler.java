package com.smartgrid.SmartGrid.SocketConfigurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SocketIoEventHandler {
   @Autowired
    private SocketIOServer socketServer;

    SocketIoEventHandler(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        /**
         * Here we create only one event listener
         * but we can create any number of listener
         * messageSendToUser is socket end point after socket connection user have to send message payload on messageSendToUser event
         */
        this.socketServer.addEventListener("fromClient2", String.class, onChatReceived());
        this.socketServer.addEventListener("records", String.class, onChatReceived2());
        this.socketServer.addEventListener("fromClient", Message.class, onChatReceived3());
        

    }
    JsonMapper jsonMapper = new JsonMapper();
    private DataListener<String> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data);
            Message message = new Message();
            try{
                message = jsonMapper.readValue(data, Message.class);
            }catch(Exception e){
                log.warn(e.getMessage());
            }         
            socketServer.getBroadcastOperations().sendEvent(message.getFrom(), "reload");
        };
    }
    private DataListener<String> onChatReceived2() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());   
            Message message = new Message();
            try{
                message = jsonMapper.readValue(data, Message.class);
            }catch(Exception e){
                log.warn(e.getMessage());
            }        
            socketServer.getBroadcastOperations().sendEvent(message.getFrom(), message.getRecords());
        };
    }
    private DataListener<Message> onChatReceived3() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
             
            socketServer.getBroadcastOperations().sendEvent(data.getFrom(), "reload");
        };
    }
    public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
            log.info("Perform operation on user connect in controller");
        }
    };


    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
            log.info("Perform operation on user disconnect in controller");
        }
    };


}
