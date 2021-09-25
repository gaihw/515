package com.zmj.demo.notice;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class KafkaConsumerListener {

    //分区绑定消费者组
    //topic为test，消费者组为consumer_group1，设置的分区为0
    @KafkaListener( topicPartitions = {@TopicPartition(topic = "test",partitionOffsets = @PartitionOffset(partition = "1",initialOffset = "-1"))},groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void kafkaListener(String message) {
        System.out.println(message);
        log.info("接收到的消息为:{}", message);
    }
//    private void consumer(ConsumerRecord consumerRecord){
//        log.info("接收到的消息为:{}",consumerRecord.value());
//
//
//    }
}
