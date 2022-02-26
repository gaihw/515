package com.zmj.demo.config.kafkaConfig;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

//@Configuration
//@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrap_servers;

    @Value("${spring.kafka.consumer.group-id}")
    private String group_id;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private String enable_auto_commit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String auto_commit_interval;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String key_deserializer;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String value_deserializer;

    @Value("${spring.kafka.consumer.atuo-offset-reset}")
    private String atuo_offset_reset_config;

    @Value("${spring.kafka.consumer.session-timeout-ms}")
    private String session_timeout_ms;

    @Value("${spring.kafka.consumer.auto-coomit-interval-ms}")
    private String auto_coomit_interval_ms;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 设置消费者工厂
        factory.setConsumerFactory(consumerFactory());
        // 消费者组中线程数量
        factory.setConcurrency(3);
        // 拉取超时时间
        factory.getContainerProperties().setPollTimeout(3000);

        // 当使用批量监听器时需要设置为true
        factory.setBatchListener(true);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>();
        // Kafka地址
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers);
        //配置默认分组，这里没有配置+在监听的地方没有设置groupId，多个服务会出现收到相同消息情况
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, group_id);
        // 是否自动提交offset偏移量(默认true)
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enable_auto_commit);
        // 自动提交的频率(ms)
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, auto_coomit_interval_ms);
        // Session超时设置
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, session_timeout_ms);
        // 键的反序列化方式
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 值的反序列化方式
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // offset偏移量规则设置：
        // (1)、earliest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，从头开始消费
        // (2)、latest：当各分区下有已提交的offset时，从提交的offset开始消费；无提交的offset时，消费新产生的该分区下的数据
        // (3)、none：topic各分区都存在已提交的offset时，从offset后开始消费；只要有一个分区不存在已提交的offset，则抛出异常
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, atuo_offset_reset_config);
        return propsMap;
    }
}
