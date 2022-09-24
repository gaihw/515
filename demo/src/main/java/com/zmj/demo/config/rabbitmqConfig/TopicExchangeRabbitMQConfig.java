package com.zmj.demo.config.rabbitmqConfig;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicExchangeRabbitMQConfig {

    /**
     * 广播的方式
     */
//    测试环境
//    public static final String fanoutExchangeName = "tfbee.test.exchange.externalLatestTicker";
//    private static final String queueBindingKey = "tfbee.test.GjFt.queue.externalLatestTicker";

//    币晟模拟环境
//    public static final String fanoutExchangeName = "tfbee.demo.exchange.externalLatestTicker";
//    private static final String queueBindingKey = "tfbee.demo.Ch3Q.queue.externalLatestTicker";

    @Value("${spring.rabbitmq.saas.exchange.externalTicker}")
    private String fanoutExchangeName;

    @Value("${spring.rabbitmq.saas.queue.externalTicker}")
    private String queueBindingKey;

    @Value("${spring.rabbitmq.l2quote.exchange.ticker}")
    private String ticker;

    @Value("${spring.rabbitmq.l2quote.exchange.kline}")
    private String kline;

    // 声明扇形交换机
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchangeName);
    }

    @Bean
    public FanoutExchange fanoutExchangeTicker(){
        return new FanoutExchange(ticker);
    }

    // 声明消息队列
    @Bean
    public Queue messageQueue() {
        return new Queue(queueBindingKey,true);
    }

    @Bean
    public Queue messageQueueTicker(){
        return new Queue("tfbee.test.l2quote.l2quote-0",true);
    }


    // 向扇形交换机上绑定队列
    @Bean
    Binding bindingQueueExchange(Queue messageQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind( messageQueue )
                .to( fanoutExchange );
    }

    @Bean
    Binding bindingQueueExchangeTicker(Queue messageQueueTicker, FanoutExchange fanoutExchangeTicker) {
        return BindingBuilder.bind( messageQueueTicker )
                .to( fanoutExchangeTicker );
    }




}