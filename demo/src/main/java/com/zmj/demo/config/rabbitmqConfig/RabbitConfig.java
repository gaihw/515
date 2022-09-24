package com.zmj.demo.config.rabbitmqConfig;

/**
 * 【请填写功能名称】
 *
 * @author gaihw
 * @date 2022/9/19 16:31
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;

/**
 Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输,
 Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。
 Queue:消息的载体,每个消息都会被投到一个或多个队列。
 Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来.
 Routing Key:路由关键字,exchange根据这个关键字进行消息投递。
 vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。
 Producer:消息生产者,就是投递消息的程序.
 Consumer:消息消费者,就是接受消息的程序.
 Channel:消息通道,在客户端的每个连接里,可建立多个channel.
 */
//@Configuration
//@Slf4j
public class RabbitConfig {


    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Resource
    private RabbitProperties rabbitProperties;

    public static final int DEFAULT_CONCURRENT = 10;//多线程消费者数量，默认10


    @Bean("customContainerFactory")
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    //单机节点
    @Bean
    @ConditionalOnProperty(name = "spring.rabbitmq.mode", havingValue = "single")
    public ConnectionFactory connectionFactorySingle(@Value("${spring.rabbitmq.host}") String host, @Value("${spring.rabbitmq.port}") int port) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitProperties.getHost(),rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setVirtualHost("/");
//        connectionFactory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        connectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());
        return connectionFactory;
    }
    //集群节点
    @Bean
    @ConditionalOnProperty(name = "spring.rabbitmq.mode", havingValue = "cluster")
    public ConnectionFactory connectionFactoryCluster(@Value("${spring.rabbitmq.addresses}") String addresses) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(rabbitProperties.getAddresses());
        cachingConnectionFactory.setUsername(rabbitProperties.getUsername());
        cachingConnectionFactory.setPassword(rabbitProperties.getPassword());
        cachingConnectionFactory.setVirtualHost("/");
//        cachingConnectionFactory.setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        cachingConnectionFactory.setPublisherReturns(rabbitProperties.isPublisherReturns());
//        log.info("集群连接工厂设置完成，连接地址{}",rabbitProperties.getAddresses());
//        log.info("集群连接工厂设置完成，连接用户{}",rabbitProperties.getUsername());
        return cachingConnectionFactory;
    }

    @Bean
    //必须是prototype类型
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }
}
