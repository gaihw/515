package com.zlm.util.websocketclient.webSocketClient;


import com.zlm.util.websocketclient.webSocketClient.inter.WebSocketService;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: create by zlm
 * @version: v1.0
 * @description: com.zlm.util.websocket.webSocketClient
 * @date:2019-10-19
 **/

@Component
public class ScoketClient implements WebSocketService {

    @Autowired
    private WebSocketClient webSocketClient;
//    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

    @Override
    public void groupSending(String message) {
        // 这里我加了6666-- 是因为我在index.html页面中，要拆分用户编号和消息的标识，只是一个例子而已
        // 在index.html会随机生成用户编号，这里相当于模拟页面发送消息
        // 实际这样写就行了 webSocketClient.send(message)
//        scheduledThreadPool.scheduleAtFixedRate(() -> {
            webSocketClient.send(message);
//        }, 30, 1, TimeUnit.SECONDS);
    }

    @Override
    public void appointSending(String name, String message) {
        // 这里指定发送的规则由服务端决定参数格式
        webSocketClient.send("TOUSER" + name + ";" + message);
    }
}
