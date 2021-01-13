package com.zlm.util.websocketclient.webSocketClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: create by zlm
 * @version: v1.0
 * @description: com.zlm.util.websocket.webSocketClient
 * @date:2019-10-19
 **/
@Slf4j
@Component
public class WebSocketConfig {
    @Bean
    public WebSocketClient webSocketClient() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
        Map<String,String> header;
        header = new HashMap<>();
        header.put("Content-Type","application/json");
        int connecttimeout = 10000;
        try {
//            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://localhost:8081/websocket/test"),new Draft_6455()) {
            WebSocketClient webSocketClient = new WebSocketClient(
                    new URI("wss://openws.58ex.com/v1/stream?streams=2001@kline_1min"),
                    new Draft_6455(),
                    header,
                    connecttimeout) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[websocket] 连接成功{}",handshakedata.getHttpStatusMessage());
                    scheduledThreadPool.scheduleAtFixedRate(() -> {
                        new ScoketClient().groupSending("{\"event\":\"ping\"}");
                    }, 30, 1, TimeUnit.SECONDS);
                }

                @Override
                public void onMessage(String message) {
                    log.info("[websocket] 收到消息={}", JSON.toJSONString(message, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat));

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[websocket] 连接错误={}", ex.getMessage());
                }
            };
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
