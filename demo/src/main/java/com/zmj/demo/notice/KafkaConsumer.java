package com.zmj.demo.notice;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = "demo",groupId = "${spring.kafka.consumer.group-id}")
    private void consumer(ConsumerRecord consumerRecord){
        log.info("接收到的消息为:{}",consumerRecord.value());

    }

}
