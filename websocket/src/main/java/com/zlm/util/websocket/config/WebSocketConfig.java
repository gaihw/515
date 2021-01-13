package com.zlm.util.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author: create by zlm
 * @version: v1.0
 * @description: com.zlm.util.websocket.config
 * @date:2019-10-18
 **/

@Component
public class WebSocketConfig {


    /**
     * ServerEndpointExporter 作用
     *
     * 这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();


    }
}
