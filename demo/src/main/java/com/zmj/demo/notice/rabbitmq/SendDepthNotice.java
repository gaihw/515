package com.zmj.demo.notice.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;


/**
 * depth多线程
 *
 * @author gaihw
 * @date 2022/9/24 16:21
 */
@Slf4j
public class SendDepthNotice implements Runnable{

    /**
     * 自增ID
     */
    public int num ;
    /**
     * 币种表
     */
    public JSONObject instruments;

    /**
     * 线程锁标识
     */
    public boolean flag = true;
    /**
     * 所属商户
     */
    public String channel;
    /**
     * mq的exchange
     */
    public String depth;
    /**
     * mq实例
     */
    public AmqpTemplate rabbitMQTemplate;

    SendDepthNotice(String depth, int num, JSONObject instruments, String channel, AmqpTemplate rabbitMQTemplate){
        super();
        this.num = num;
        this.instruments = instruments;
        this.channel = channel;
        this.depth = depth;
        this.rabbitMQTemplate = rabbitMQTemplate;
    }

    @Override
    public void run() {
//        if (flag) {
//            while (true) {
//                synchronized (SendDepthNotice.class) {
//                    depthRun(depth,instruments,channel,rabbitMQTemplate);
//                }
//            }
//        }else {
//            while (true)
//                depthRun(depth,instruments,channel,rabbitMQTemplate);
//        }
        while (true)
            depthRun(depth,instruments,channel,rabbitMQTemplate);
    }

    /**
     * 发送消息
     * @param depth
     * @param instruments
     * @param channel
     * @param rabbitMQTemplate
     */
    public synchronized void depthRun(String depth,JSONObject instruments,  String channel,AmqpTemplate rabbitMQTemplate){
        long time ;
        Random random = new Random();
        int rTmp;
        String p;
        String routingKey;
        for (String key:instruments.keySet()) {
            rTmp = random.nextInt(100);
            time = System.currentTimeMillis();
            p = "{\"asks\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.05)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.03)))) + "," + random.nextInt(1000) + "]],\"bids\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.02)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + "," + random.nextInt(1000) + "]]}";
            routingKey = "market_" + channel + "." + key + "_depth_step0_" + num + "_" + time;
            rabbitMQTemplate.convertAndSend(depth, routingKey, p);
            log.info("======第{}推消息======routingKey={}======>{}", num, routingKey, p);
//            try {
////                Thread.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            num ++;
        }
    }
}
