package com.zmj.demo.notice.rabbitmq;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
public class RabbitMqProducer {
    @Autowired
    private AmqpTemplate rabbitMQTemplate;

    @RequestMapping(value = "/rabbitmq",method = RequestMethod.GET)
    public void send() throws InterruptedException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("symbolId","1");
//        jsonObject.put("eventTime",System.currentTimeMillis());
//        jsonObject.put("recieveTime",System.currentTimeMillis());
        jsonObject.put("tickerSource","BINANCE");
        jsonObject.put("high",1);
        jsonObject.put("low",2);
        jsonObject.put("volume",33);
        jsonObject.put("amount",44);
        jsonObject.put("open",3);
        System.out.println(jsonObject);

        Random random = new Random();
        int rTmp = 0;

        int i = 1;
        while (true){
            jsonObject.put("eventTime", System.currentTimeMillis());
            jsonObject.put("recieveTime", System.currentTimeMillis());
            rTmp = random.nextInt(100);
            if (rTmp >= 50) {
                jsonObject.put("last",16000);
            }else {
                jsonObject.put("last",22000);
            }
            rabbitMQTemplate.convertAndSend("tfbee.test.exchange.externalLatestTicker","",jsonObject.toString());
            log.info("======第{}推消息======>{}",i,jsonObject);
            i ++;

            Thread.sleep(5000);

        }
    }
}
