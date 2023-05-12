package com.zmj.demo.config.websocket;

import com.zmj.demo.dao.dev.OptionsInfoDao;
import com.zmj.demo.wsClient.ReConnectWSClient;
import com.zmj.demo.wsClient.WSClient;
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

    @Autowired
    private OptionsInfoDao optionsInfoDao;

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
            ReConnectWSClient webSocketClient = new ReConnectWSClient(new URI(socketDomain.getUrl()),
                    socketDomain.getKey(),
                    socketDomain.getReConnectNum(),
                    // 字符串消息处理
                    msg -> {
                        // todo 字符串消息处理
                        System.out.println("字符串消息:" + msg);
                    },
                    null,
                    // 异常回调
                    error -> {
                        // todo 字符串消息处理
                        System.out.println("异常:" + error.getMessage());
                    },
                    optionsInfoDao.getList());
            try {
                webSocketClient.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return webSocketClient;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
