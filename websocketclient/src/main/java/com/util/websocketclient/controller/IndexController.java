package com.util.websocketclient.controller;


import com.util.websocketclient.webSocketClient.ScoketClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
public class IndexController {

    @Autowired
    private ScoketClient webScoketClient;


    @ApiOperation(value ="sendMessage",notes = "sendMessage")
    @GetMapping("/sendMessage")
    public String sendMessage(String message){
        webScoketClient.groupSending(message);
        return message;
    }
}
