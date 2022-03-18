package com.zmj.demo.config.websocket;

import com.zmj.demo.client.WebSocketClient;
import com.zmj.demo.domain.websocket.SocketDomain;
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
            WebSocketClient webSocketClient = new WebSocketClient(new URI(socketDomain.getUrl()));
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
