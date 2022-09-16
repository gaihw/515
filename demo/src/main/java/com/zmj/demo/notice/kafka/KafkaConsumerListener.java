package com.zmj.demo.notice.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;


//@Component
@Slf4j
public class KafkaConsumerListener {

    //分区绑定消费者组
    //topic为test，消费者组为consumer_group1，设置的分区为0

    /**
     * 配置topic和分区,可以配置多个
     * topic为队列名称
     * partitions表示值的的分区，这里指定了0
     * partitionOffsets表示详细的指定分区，partition表示那个分区，initialOffset表示Offset的初始位置
     * @param message
     */
    @KafkaListener( topicPartitions =
            {@TopicPartition(topic = "test",
                    partitions = { "0" },
                    partitionOffsets = @PartitionOffset(partition = "1",initialOffset = "-1"))},
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void kafkaListener(String message) {
        System.out.println(message);
        log.info("接收到的消息为:{}", message);
    }
}
