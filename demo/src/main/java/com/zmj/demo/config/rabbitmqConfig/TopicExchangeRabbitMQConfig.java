package com.zmj.demo.config.rabbitmqConfig;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeRabbitMQConfig {

    /**
     * 广播的方式
     */
    public static final String fanoutExchangeName = "tfbee.test.exchange.externalLatestTicker";

    private static final String queueBindingKey = "tfbee.test.GjFt.queue.externalLatestTicker";

    // 声明扇形交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    // 声明消息队列
    @Bean
    public Queue messageQueue() {
        return new Queue(queueBindingKey,true);
    }


    // 向扇形交换机上绑定队列
    @Bean
    Binding bindingQueueExchange(Queue messageQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind( messageQueue )
                .to( fanoutExchange );
    }


}