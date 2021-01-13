package com.zmj.rabbitmq;
import com.zmj.domain.orther.DepositChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MsgConsumer {

    public static final String FANOUT_QUEUE_NAME = "test_fanout_queue";
    public static final String FANOUT_QUEUE_NAME1 = "test_fanout_queue1";
    public static final String TEST_FANOUT_EXCHANGE = "testFanoutExchange";

    public static final String DIRECT_QUEUE_NAME = "test_direct_queue";
    public static final String TEST_DIRECT_EXCHANGE = "testDirectExchange";
    public static final String DIRECT_ROUTINGKEY = "test";

    public static final String TOPIC_QUEUE_NAME = "test_topic_queue";
    public static final String TEST_TOPIC_EXCHANGE = "testTopicExchange";
    public static final String TOPIC_ROUTINGKEY = "test.*";


    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = FANOUT_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = TEST_FANOUT_EXCHANGE, type = "fanout"))
                    })
    @RabbitHandler
    public void processFanoutMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Fanout message : " + msg);
    }

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = FANOUT_QUEUE_NAME1, durable = "true"),
                                    exchange = @Exchange(value = TEST_FANOUT_EXCHANGE, type = "fanout"))
                    })
    @RabbitHandler
    public void processFanout1Msg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Fanout1 message : " + msg);
    }

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = DIRECT_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = TEST_DIRECT_EXCHANGE),
                                    key = DIRECT_ROUTINGKEY)
                    })
    @RabbitHandler
    public void processDirectMsg(Message massage) {
        String msg = new String(massage.getBody(), StandardCharsets.UTF_8);
        log.info("received Direct message : " + msg);
    }

    @RabbitListener(
            bindings =
                    {
                            @QueueBinding(value = @Queue(value = TOPIC_QUEUE_NAME, durable = "true"),
                                    exchange = @Exchange(value = TEST_TOPIC_EXCHANGE, type = "topic"),
                                    key = TOPIC_ROUTINGKEY)
                    })
    @RabbitHandler
    public void processTopicMsg(Message message) {
//        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
//        log.info("received Topic message : " + msg);
//        JSONObject jsonObject = JSONObject.parseObject(msg);
//        System.out.println("txid::"+jsonObject.getString("txid"));


        //字节码转化为对象
        byte[] bytes=message.getBody();
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream oi = null;
        try {
            oi = new ObjectInputStream(bi);
            DepositChain testMQ=(DepositChain) oi.readObject();
            log.info("received Topic message : " + testMQ);
            System.out.println("消费者直接接收对象进行消费（进行处理一些业务）："+testMQ.getTxid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
