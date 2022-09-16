package com.zmj.demo.domain.websocket;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "socket.config")
@Data
public class SocketDomain {

    private String url;
    private String key;
    private int reConnectNum;
}
