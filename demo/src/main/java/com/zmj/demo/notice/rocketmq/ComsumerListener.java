package com.zmj.demo.notice.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * roketmq 消费者
 *
 * @author gaihw
 * @date 2023/5/17 11:33
 */
//@RocketMQMessageListener(topic = "test-topic",nameServer = "${spring.rocketmq.nameServer}",consumerGroup = "${spring.rocketmq.consumer.group}", selectorExpression = "test-tag")
//@Component
@Slf4j
public class ComsumerListener implements RocketMQListener<MessageBody> {

    @Override
    public void onMessage(MessageBody messageBody) {

        System.out.println(messageBody);

    }
}

