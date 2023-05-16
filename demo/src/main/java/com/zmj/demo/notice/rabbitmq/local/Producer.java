package com.zmj.demo.notice.rabbitmq.local;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 本地 rabbitmq 生产者
 *
 * fanout 不需要绑定routing-key
 * direct、topic 需要绑定routing-key,不绑定也可以
 *
 *
 * @author gaihw
 * @date 2023/5/16 14:33
 */
@RestController
@Slf4j
public class Producer {
    @Autowired
    private AmqpTemplate rabbitMQTemplate;

    @Value("${spring.rabbitmq.test.exchangeTopic}")
    private String exchangeTopic;

    @Value("${spring.rabbitmq.test.exchangeFanout}")
    private String exchangeFanout;

    @Value("${spring.rabbitmq.test.exchangeDirect}")
    private String exchangeDirect;

    @Value("${spring.rabbitmq.test.exchangeHeaders}")
    private String exchangeHeaders;

    /**
     * topic模式的生产者,routing-key模式
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeTopic",method = RequestMethod.GET)
    public void exchangeTopic(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeTopic,"test",msg);
    }

    /**
     * topic模式的生产者,无routing-key模式
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeTopicNoRoutingKey",method = RequestMethod.GET)
    public void exchangeTopicNoRoutingKey(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeTopic,"",msg);
    }

    /**
     * topic模式的生产者,routing-key模式，test1
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeTopic1",method = RequestMethod.GET)
    public void exchangeTopic1(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeTopic,"test1",msg);
    }

    /**
     * fanout模式的生产者,无routing-key模式
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeFanoutNoRoutingKey",method = RequestMethod.GET)
    public void exchangeFanoutNoRoutingKey(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeFanout,"",msg);
    }

    /**
     * fanout模式的生产者,routing-key模式,test_f
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeFanout",method = RequestMethod.GET)
    public void exchangeFanout(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeFanout,"test_f",msg);
    }

    /**
     * direct模式的生产者，有routing-key模式，test_d
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeDirect",method = RequestMethod.GET)
    public void exchangeDirect(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeDirect,"test_d",msg);
    }

    /**
     * direct模式的生产者，无routing-key模式
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeDirectNoRoutingKey",method = RequestMethod.GET)
    public void exchangeDirectNoRoutingKey(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeDirect,"",msg);
    }

    /**
     * headers模式的生产者
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeHeaders",method = RequestMethod.GET)
    public void exchangeHeaders(@RequestParam("msg") String msg) throws InterruptedException {
        rabbitMQTemplate.convertAndSend(exchangeHeaders,"",msg);
    }

    /**
     * headers模式的生产者,绑定header
     * @param msg
     * @throws InterruptedException
     */
    @RequestMapping(value = "/rabbitmq/test/exchangeHeadersMatch",method = RequestMethod.GET)
    public void exchangeHeadersMatch(@RequestParam("msg") String msg) throws InterruptedException {

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("header","header1");
        Message message = new Message(msg.getBytes(),messageProperties);
        rabbitMQTemplate.convertAndSend(exchangeHeaders,"",message);
    }
}
