package com.zmj.demo.notice.rabbitmq.local;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 本地 消费者模式
 *
 * @author gaihw
 * @date 2023/5/16 14:40
 */
//@Component
public class Consumer {

    /**
     * topic模式的消费者，routing-key模式
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-0")
    @RabbitHandler
    public void getQueueMessage(String  msg) {
        System.out.println("exchangeTopic，routing-key模式，test， Receive , Msg: " + msg);
    }

    /**
     * topic模式的消费者，无routing-key模式
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-1")
    @RabbitHandler
    public void getQueueMessage1(String  msg) {
        System.out.println("exchangeTopic无routing-key模式， Receive , Msg: " + msg);
    }

    /**
     * topic模式的消费者，routing-key模式，test1
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-2")
    @RabbitHandler
    public void getQueueMessage2(String  msg) {
        System.out.println("exchangeTopic，routing-key模式，test1， Receive , Msg: " + msg);
    }

    /**
     * fanout模式的消费者，无routing-key模式
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-f-0")
    @RabbitHandler
    public void getQueueMessage3(String  msg) {
        System.out.println("exchangeFanout，无routing-key模式， Receive , Msg: " + msg);
    }

    /**
     * fanout模式的消费者，无routing-key模式
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-f-1")
    @RabbitHandler
    public void getQueueMessage4(String  msg) {
        System.out.println("exchangeFanout，无routing-key模式1， Receive , Msg: " + msg);
    }

    /**
     * fanout模式的消费者，routing-key模式,test_f
     * 一、fanout
     *
     * 发布/订阅的广播模式，它会把发送到该交换器的消息发送到所有与该交换器绑定的队列中。不需要指定Routingkey和BindingKey。比如两个队列绑定了同一个交换器，那么2个队列都会同时收到消息。
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-f-2")
    @RabbitHandler
    public void getQueueMessage5(String  msg) {
        System.out.println("exchangeFanout，routing-key模式，test_f， Receive , Msg: " + msg);
    }

    /**
     * direct模式的消费者，routing-key模式，test_d
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-d-0")
    @RabbitHandler
    public void getQueueMessage6(String  msg) {
        System.out.println("exchangeDirect，routing-key模式，test_d， Receive , Msg: " + msg);
    }

    /**
     * direct模式的消费者，无routing-key模式
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-d-1")
    @RabbitHandler
    public void getQueueMessage7(String  msg) {
        System.out.println("exchangeDirect，无routing-key模式， Receive , Msg: " + msg);
    }

    /**
     * headers模式的消费者
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-h-0")
    @RabbitHandler
    public void getQueueMessage8(String  msg) {
        System.out.println("exchangeHeaders，未绑定header， Receive , Msg: " + msg);
    }

    /**
     * headers模式的消费者,绑定header
     * @param msg
     */
    @RabbitListener(queues = "queues-consumer-h-1")
    @RabbitHandler
    public void getQueueMessage9(String  msg) {
        System.out.println("exchangeHeaders，绑定header， Receive , Msg: " + msg);
    }

    @RabbitListener(queues = "queues-consumer-h-2")
    @RabbitHandler
    public void getQueueMessage10(String  msg) {
        System.out.println("exchangeHeaders，绑定header1， Receive , Msg: " + msg);
    }
}
