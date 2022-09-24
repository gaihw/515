package com.zmj.demo.notice.rabbitmq;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class RabbitMqConsumer {


    @Value("${spring.rabbitmq.saas.exchange.externalTicker}")
    private String fanoutExchangeName;

    @Value("${spring.rabbitmq.saas.queue.externalTicker}")
    private String queueBindingKey;


//    @RabbitListener(queues = "tfbee.demo.Ch3Q.queue.externalLatestTicker")
//    @RabbitListener(queues = "tfbee.test.GjFt.queue.externalLatestTicker")
//    @RabbitListener(queues = queueBindingKey)
    @RabbitHandler
    public void getQueueMessage(String  msg) {
        System.out.println(queueBindingKey+" Receive , Msg: " + msg);
    }
}
