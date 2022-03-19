package com.config;

import com.client.MyWebSocketClient;
import com.domain.SocketDomain;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class WebSocketConfig {

    @Autowired
    private SocketDomain socketDomain;

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            MyWebSocketClient webSocketClient = new MyWebSocketClient(new URI(socketDomain.getUrl()));
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
