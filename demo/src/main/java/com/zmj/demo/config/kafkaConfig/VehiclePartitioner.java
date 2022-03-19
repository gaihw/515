package com.zmj.demo.config.kafkaConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

@Slf4j
public class VehiclePartitioner implements Partitioner {
    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        log.info("topic:{},共有分区:{}",s,cluster.partitionsForTopic(s));
        return 1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
