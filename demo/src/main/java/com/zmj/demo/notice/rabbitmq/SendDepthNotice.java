package com.zmj.demo.notice.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;


/**
 * kline多线程
 *
 * @author gaihw
 * @date 2022/9/24 16:21
 */
@Slf4j
public class SendKlineNotice implements Runnable{

    /**
     * 自增ID
     */
    public int num ;
    /**
     * 币种表
     */
    public JSONObject instruments;
    /**
     * k线刻度
     */
    public List<String> scale;
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
    public String kline;
    /**
     * mq实例
     */
    public AmqpTemplate rabbitMQTemplate;

    SendKlineNotice(String kline,int num,JSONObject instruments,List<String> scale,String channel,AmqpTemplate rabbitMQTemplate){
        super();
        this.num = num;
        this.instruments = instruments;
        this.scale = scale;
        this.channel = channel;
        this.kline = kline;
        this.rabbitMQTemplate = rabbitMQTemplate;
    }

    @Override
    public void run() {
        if (flag) {
            while (true) {
                synchronized (SendKlineNotice.class) {
                    klineRun(kline,instruments,scale,channel,rabbitMQTemplate);
                }
            }
        }else {
            while (true)
                klineRun(kline,instruments,scale,channel,rabbitMQTemplate);
        }
    }

    /**
     * 发送消息
     * @param kline
     * @param instruments
     * @param scale
     * @param channel
     * @param rabbitMQTemplate
     */
    public synchronized void klineRun(String kline,JSONObject instruments, List<String> scale,  String channel,AmqpTemplate rabbitMQTemplate){
        long time ;
        Random random = new Random();
        int rTmp;
        String p;
        String routingKey;
        for (String key : instruments.keySet()) {
            for (String s : scale) {

                time = System.currentTimeMillis();
                rTmp = random.nextInt(100);
                p = "{\"status\":\"ok\",\"event_rep\":\"\",\"channel\":\"market_" + channel + "." + key + "_kline_" + s + "\",\"ts\":" + time + ",\"tick\":" +
                        "{\"id\":" + num + ",\"amount\":" + random.nextInt(100000) + ",\"open\":" + instruments.getBigDecimal(key) + ",\"close\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"high\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"low\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"vol\":" + random.nextInt(1000000) + ",\"mrid\":-1000}}";

                routingKey = "market_" + channel + "." + key + "_kline_" + s + "_-1000_" + time + "_" + time + "_" + time;

                try {
                    rabbitMQTemplate.convertAndSend(kline, routingKey, GzipUtil.compress(p));
                    log.info("======第{}推消息======routingKey={}======>{}", num, routingKey, p);
//                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                num ++;
            }
        }
    }
}
