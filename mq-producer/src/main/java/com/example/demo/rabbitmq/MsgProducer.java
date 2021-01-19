package com.example.demo.rabbitmq;
import com.example.demo.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
@Slf4j
public class MsgProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send2FanoutTestQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_FANOUT_EXCHANGE,
                "", massage);
    }

    public void send2DirectTestQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_DIRECT_EXCHANGE,
                RabbitConfig.DIRECT_ROUTINGKEY, massage);
    }

    public void send2TopicTestAQueue(String massage){
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_TOPIC_EXCHANGE,
                "test.aaa", massage);
    }

    public void send2TopicTestBQueue(String massage){
        log.info("send2TopicTestBQueue:"+massage);
        rabbitTemplate.convertAndSend(RabbitConfig.TEST_TOPIC_EXCHANGE,
                "test.bbb", massage);
    }

}
