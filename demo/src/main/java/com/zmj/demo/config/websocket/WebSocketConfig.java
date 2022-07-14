package com.zmj.demo.config.websocket;

import com.zmj.demo.client.ReConnectWSClient;
import com.zmj.demo.client.WSClient;
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

//    @Bean
    public WSClient wSClient() {
        try {
            WSClient webSocketClient = new WSClient(new URI(socketDomain.getUrl()));
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public ReConnectWSClient reConnectWSClient() {
        try {
            ReConnectWSClient webSocketClient = new ReConnectWSClient(new URI(socketDomain.getUrl()));
            webSocketClient.connect();
            return webSocketClient;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
