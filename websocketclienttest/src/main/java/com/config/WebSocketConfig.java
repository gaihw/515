package com.config;

import com.client.MyWebSocketClient;
import org.java_websocket.client.WebSocketClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class WebSocketConfig {
    @Bean
    public WebSocketClient webSocketClient() {
        try {
//            MyWebSocketClient webSocketClient = new MyWebSocketClient(new URI("ws://localhost:8081/websocket/test"));//
            MyWebSocketClient webSocketClient = new MyWebSocketClient(new URI("wss://ws.chimchim.top/"));
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
