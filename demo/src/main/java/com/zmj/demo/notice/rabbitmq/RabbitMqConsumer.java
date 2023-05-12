package com.zmj.demo.notice.rabbitmq;

import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 监听mq消息
 */
//@Component
public class RabbitMqConsumer {


    @Value("${spring.rabbitmq.saas.exchange.externalTicker}")
    private String fanoutExchangeName;

    @Value("${spring.rabbitmq.saas.queue.externalTicker}")
    private String queueBindingKey;

    @Value("${spring.rabbitmq.options.exchange.optionTicker}")
    private String exchangeOptionTicker;

    @Value("${spring.rabbitmq.options.queue.optionTicker}")
    private String queueOptionTicker;


//    @RabbitListener(queues = "tfbee.demo.Ch3Q.queue.externalLatestTicker")
//    @RabbitListener(queues = "tfbee.dev.xxOi.queue.optionTicker")
    @RabbitHandler
    public void getQueueMessage(String  msg) {
        System.out.println(queueOptionTicker+" Receive , Msg: " + msg);
    }
}
