package com.zlm.util.websocketclient.controller;


import com.zlm.util.websocketclient.webSocketClient.ScoketClient;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: create by zlm
 * @version: v1.0
 * @description: com.zlm.util.websocket.controller
 * @date:2019-10-19
 **/
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
