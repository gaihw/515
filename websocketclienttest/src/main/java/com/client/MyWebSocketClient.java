package com.client;

import com.server.RedisService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MyWebSocketClient extends WebSocketClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebSocketClient.class);

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private RedisService redisService;

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

    @Override
    public void onOpen(ServerHandshake arg0) {
        // TODO Auto-generated method stub
        LOGGER.info("------ MyWebSocket onOpen ------");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"btc\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"eth\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"ltc\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"trx\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"xrp\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"bch\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"eos\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"fil\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"link\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"dot\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"doge\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"shib\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");

        scheduledThreadPool.scheduleAtFixedRate(() -> {
            webSocketClient.send("{\"event\":\"ping\"}");
                    }, 30, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        LOGGER.info("------ MyWebSocket onClose ------{}",arg1);
    }

    @Override
    public void onError(Exception arg0) {
        // TODO Auto-generated method stub
        LOGGER.info("------ MyWebSocket onError ------{}",arg0);
    }

    @Override
    public void onMessage(String arg0) {
        // TODO Auto-generated method stub
        LOGGER.info("-------- 接收到服务端数据： " + arg0 + "--------");
        if (!arg0.contains("event")){
            JSONObject jsonObject = JSONObject.parseObject(arg0);
            System.out.println(jsonObject.getString("base")+"==="+jsonObject.getJSONObject("data").getBigDecimal("p"));
            try {
                redisService.addValue(jsonObject.getString("base"),jsonObject.getJSONObject("data").getString("p"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
