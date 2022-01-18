package com.client;

import com.alibaba.fastjson.JSONObject;
import com.server.RedisService;
import com.util.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
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
        LOGGER.info("------ MyWebSocket onOpen ------");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"btc\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"eth\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"ltc\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"trx\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"xrp\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"bch\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"eos\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"fil\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"link\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"dot\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"doge\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"shib\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");

//        scheduledThreadPool.scheduleAtFixedRate(() -> {
//            System.out.println("发送：：：{\"ping\":"+System.currentTimeMillis()+"}");
//            webSocketClient.send("{\"ping\":"+System.currentTimeMillis()+"}");
//                    }, 0, 5, TimeUnit.SECONDS);
    }


    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        LOGGER.info("------ MyWebSocket onClose ------{}", arg1);
    }

    @Override
    public void onError(Exception arg0) {
        LOGGER.info("------ MyWebSocket onError ------{}", arg0);
    }

    @Override
    public void onMessage(ByteBuffer buffer) {
        //获取buffer中有效大小
        int len = buffer.limit() - buffer.position();
        byte[] bytes = new byte[len];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = buffer.get();
        }
        String res = GzipUtil.uncompressToString(bytes);
        LOGGER.info("-------- ByteBuffer接收到服务端数据： " + res + "--------");

        if (res.contains("ping")) {
            String time = JSONObject.parseObject(GzipUtil.uncompressToString(bytes)).getString("ping");
            webSocketClient.send("{\"pong\": \"" + time + "\"}");
            System.out.println("发送：：：{\"pong\": \"" + time + "\"}");
        }
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_ticker\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ethusdt_kline_1min\",\"cb_id\":\"10001\"}}");
        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ethusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        if (!res.contains("market_btcusdt_mark_price")) {
//            JSONObject jsonObject = JSONObject.parseObject(res);
//            System.out.println(jsonObject.getJSONObject("data").getBigDecimal("mp"));
//            try {
//                redisService.addValue("btc", jsonObject.getJSONObject("data").getString("mp"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

    @Override
    public void onMessage(String s) {
        LOGGER.info("-------- String接收到服务端数据： " + s + "--------");
//        if (!arg0.contains("event")){
//            JSONObject jsonObject = JSONObject.parseObject(arg0);
//            System.out.println(jsonObject.getString("base")+"==="+jsonObject.getJSONObject("data").getBigDecimal("p"));
//            try {
//                redisService.addValue(jsonObject.getString("base"),jsonObject.getJSONObject("data").getString("p"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}
