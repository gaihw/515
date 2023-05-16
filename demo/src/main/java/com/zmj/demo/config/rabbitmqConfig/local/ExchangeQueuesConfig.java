package com.zmj.demo.config.rabbitmqConfig.local;

import org.springframework.amqp.core.*;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * exchange绑定queues
 *
 * @author gaihw
 * @date 2023/5/16 14:56
 */

@Configuration
public class ExchangeQueuesConfig {
    //=====================================topic模式=====================================
    /**
     * 定义exchange，topic模式
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("amq.topic");
    }

    /**
     * 定义queue，queues-consumer-2
     * @return
     */
    @Bean
    public Queue messageQueue(){
        return new Queue("queues-consumer-2",true);
    }

    /**
     * 给topic模式的exchange交换机，绑定queues-consumer-2队列
     * @param messageQueue
     * @param topicExchange
     * @return
     */
    @Bean
    Binding bindingQueueExchange(Queue messageQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind( messageQueue )
                .to( topicExchange )
                .with("test1");
    }


    //=====================================fanout模式=====================================
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("amq.fanout");
    }
    @Bean
    public Queue messageQueue1(){
        return new Queue("queues-consumer-f-1",true);
    }
    @Bean
    Binding bindingQueueExchange1(Queue messageQueue1, FanoutExchange fanoutExchange){
        return BindingBuilder.bind(messageQueue1)
                .to(fanoutExchange);

    }

    //=====================================direct模式=====================================
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("amq.direct");
    }

    @Bean
    public Queue messageQueue2(){
        return new Queue("queues-consumer-d-1",true);
    }

    /**
     * direct模式，在给exchange绑定queue时，一定要绑定routing-key，routing-key可以为空的字符串
     * @param messageQueue2
     * @param directExchange
     * @return
     */
    @Bean
    Binding bindingQueueExchange2(Queue messageQueue2, DirectExchange directExchange){
        return BindingBuilder.bind(messageQueue2)
                .to(directExchange)
                .with("");
    }
    //=====================================header模式=====================================
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange("amq.headers");
    }

    @Bean
    public Queue messageQueue3(){
        return new Queue("queues-consumer-h-1",true);
    }

    /**
     * header模式，在给exchange绑定queue,绑定header，条件为  header:header1
     * @param messageQueue3
     * @param headersExchange
     * @return
     */
    @Bean
    Binding bindingQueueExchange3(Queue messageQueue3, HeadersExchange headersExchange){
        return BindingBuilder.bind(messageQueue3)
                .to(headersExchange)
                .where("header")
                .matches("header1");
    }

    @Bean
    public Queue messageQueue4(){
        return new Queue("queues-consumer-h-2",true);
    }

    /**
     * header模式，在给exchange绑定queue,绑定header，条件为  header_111:header_222
     * @param messageQueue4
     * @param headersExchange
     * @return
     */
    @Bean
    Binding bindingQueueExchange4(Queue messageQueue4, HeadersExchange headersExchange){
        return BindingBuilder.bind(messageQueue4)
                .to(headersExchange)
                .where("header_111")
                .matches("header_222");
    }
}
