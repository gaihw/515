package com.controller;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @Autowired
    private WebSocketClient webSocketClient;

    @RequestMapping("subscribe")
    public String subscribe() {
        // webSocketClient.connect();
        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"indexes\",\"type\":\"ticker_swap\",\"base\":\"bch\",\"quote\":\"usd\",\"zip\":false}}");
        return "发送订阅成功！！！";
    }
}
