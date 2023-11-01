package com.zmj.demo.notice.rabbitmq;


import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class RabbitMqProducer {

    @Autowired
    private AmqpTemplate rabbitMQTemplate;

    @Value("${spring.rabbitmq.saas.exchange.externalTicker}")
    private String externalTicker;

    @Value("${spring.rabbitmq.saas.exchange.externalDepth}")
    private String externalDepth;

    @Value("${spring.rabbitmq.l2quote.exchange.ticker}")
    private String ticker;

    @Value("${spring.rabbitmq.l2quote.exchange.kline}")
    private String kline;

    @Value("${spring.rabbitmq.l2quote.exchange.depth}")
    private String depth;

    @Value("${spring.rabbitmq.l2quote.exchange.indexPrice}")
    private String indexPrice;

    @Value("${spring.rabbitmq.l2quote.exchange.markPrice}")
    private String markPrice;

    @Value("${spring.rabbitmq.l2quote.exchange.fundingrate}")
    private String fundingrate;

    @Value("${spring.rabbitmq.l2quote.exchange.spotfills}")
    private String spotfills;

    @Value("${spring.rabbitmq.saas.channel}")
    private String channel;

    @Value("${spring.rabbitmq.options.exchange.optionTicker}")
    private String exchangeOptionTicker;

    @Value("${spring.rabbitmq.options.queue.optionTicker}")
    private String queueOptionTicker;

    @RequestMapping(value = "/rabbitmq/options",method = RequestMethod.GET)
    public void options() throws InterruptedException {
        String msg ;
        for (int i = 0; i < 100; i++) {
            msg = "{\"currency\":\"BTC\",\"instrumentName\":\"BTC-13APR23-32500-P\",\"marketType\":\"EXTERNAL_OPTION_TICKER\",\"tickerSource\":\"DERIBIT\",\"ask\":3310.000,\"askAmount\":10.0,\"bid\":1033.000,\"bidAmount\":15.0,\"lastPrice\":0.00,\"markPrice\":2566.549599,\"markIV\":56.81,\"bidIV\":0.0,\"askIV\":0.0,\"vega\":3.36045,\"theta\":-80.51686,\"rho\":-0.82616,\"gamma\":0.00023,\"delta\":-0.87047,\"timestamp\":"+System.currentTimeMillis()+"}";
            // 使用queue推送消息
            rabbitMQTemplate.convertAndSend(queueOptionTicker,msg);
            Thread.sleep(2000);
        }

    }

    @RequestMapping(value = "/rabbitmq/externalTicker",method = RequestMethod.GET)
    public void externalTicker(@RequestParam("flag") int flag) throws InterruptedException, IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("symbolId","1");
//        jsonObject.put("eventTime",System.currentTimeMillis());
//        jsonObject.put("recieveTime",System.currentTimeMillis());
        jsonObject.put("tickerSource","BINANCE");
        jsonObject.put("high",1);
        jsonObject.put("low",2);
        jsonObject.put("volume",33);
        jsonObject.put("amount",44);
        jsonObject.put("open",3);
        System.out.println(jsonObject);

        Random random = new Random();
        int rTmp = 0;
        int i = 0;
        while (flag == 1){
            jsonObject.put("eventTime", System.currentTimeMillis());
            jsonObject.put("recieveTime", System.currentTimeMillis());
            rTmp = random.nextInt(100);
//            jsonObject.put("last",15000);
            if (rTmp <= 20) {
                jsonObject.put("last",15000);
            }else if (rTmp >20 && rTmp <= 50){
                jsonObject.put("last",18000);
            }else if (rTmp >50 && rTmp <= 70){
                jsonObject.put("last",21000);
            }else {
                jsonObject.put("last",23000);
            }
            rabbitMQTemplate.convertAndSend(externalTicker,"",jsonObject.toString());
            log.info("======第{}推消息======>{}",i,jsonObject);
            Thread.sleep(1);
            i ++;
        }
    }


    @RequestMapping(value = "/rabbitmq/externalDepth",method = RequestMethod.GET)
    public void externalDepth(@RequestParam("flag") int flag) throws InterruptedException, IOException {
        JSONObject instruments = new JSONObject();
        instruments.put("btcusdt", BigDecimal.valueOf(26585.7));
        instruments.put("ethusdt", BigDecimal.valueOf(1763.02));
        instruments.put("ltcusdt", BigDecimal.valueOf(78.94));
        instruments.put("eosusdt", BigDecimal.valueOf(0.8710));
        instruments.put("bchusdt", BigDecimal.valueOf(113.64));
        instruments.put("trxusdt", BigDecimal.valueOf(0.06));
        instruments.put("filusdt", BigDecimal.valueOf(4.292));
        instruments.put("linkusdt", BigDecimal.valueOf(6.307));
        instruments.put("dotusdt", BigDecimal.valueOf(5.225));
        instruments.put("xrpusdt", BigDecimal.valueOf(0.4365));
        instruments.put("dogeusdt", BigDecimal.valueOf(0.07135));
        instruments.put("shibusdt", BigDecimal.valueOf(0.000008611));
        instruments.put("adausdt", BigDecimal.valueOf(0.36227));
        instruments.put("maticusdt", BigDecimal.valueOf(0.83177));
        instruments.put("solusdt", BigDecimal.valueOf(20.0835));
        instruments.put("bnbusdt", BigDecimal.valueOf(304.87));
        instruments.put("avaxusdt", BigDecimal.valueOf(14.841));
        instruments.put("manausdt", BigDecimal.valueOf(0.4444));
        instruments.put("axsusdt", BigDecimal.valueOf(6.65085));
        instruments.put("uniusdt", BigDecimal.valueOf(4.9860));
        instruments.put("chzusdt", BigDecimal.valueOf(0.10510));
        instruments.put("apeusdt", BigDecimal.valueOf(3.16650));
        instruments.put("ftmusdt", BigDecimal.valueOf(0.36280));
        instruments.put("dashusdt", BigDecimal.valueOf(39.25));
        instruments.put("atomusdt", BigDecimal.valueOf(10.948));
        instruments.put("arbusdt", BigDecimal.valueOf(1.0762));
        instruments.put("xlmusdt", BigDecimal.valueOf(0.08918));
        instruments.put("suiusdt", BigDecimal.valueOf(1.034));
        instruments.put("pepeusdt", BigDecimal.valueOf(0.0000011971));

        int num = 1;
//        SendDepthNotice sendDepthNotice = new SendDepthNotice(depth,num,instruments,channel,rabbitMQTemplate);
//        Thread t1 = new Thread(sendDepthNotice);
//        Thread t2 = new Thread(sendDepthNotice);
//        Thread t3 = new Thread(sendDepthNotice);
//
//        t1.start();
////        Thread.sleep(3);
////        sendDepthNotice.flag = false;
//        t2.start();
//        t3.start();

        Random random = new Random();
        int rTmp ;
        String p;
        int i = 3780;
        String routingKey;
        long time;
        while (flag == 1){
            for (String key:instruments.keySet()) {
                rTmp = random.nextInt(100);
                time = System.currentTimeMillis();
                p = "{\"asks\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.05)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.03)))) + "," + random.nextInt(1000) + "]],\"bids\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.02)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + "," + random.nextInt(1000) + "]]}";
                routingKey = "market_" + channel + "." + key + "_depth_step0_" + i + "_" + time;
                rabbitMQTemplate.convertAndSend(externalDepth, "",p);
                log.info("======第{}推消息======routingKey={}======>{}", i, routingKey, p);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i ++;
            }
        }

    }


    @RequestMapping(value = "/rabbitmq/legendSub",method = RequestMethod.GET)
    public void legendSub() throws InterruptedException, IOException {
        String p;
        long i = 30009189338l;
        while (true){
            p = "[{\"id\":10000393317,\"order-type\":\"buy-limit\",\"symbol\":\"xxOi.BTC-231018-28750-C\",\"publish-ts\":1697613265901,\"ts\":1697613265900,\"items\":[{\"order-id\":-100,\"price\":5.00000,\"role\":\"maker\",\"seq-id\":10000393316,\"filled-amount\":1.000,\"unfilled-amount\":0,\"state\":\"filled\"},{\"order-id\":-100,\"price\":5.0000000000000000,\"role\":\"taker\",\"seq-id\":10000393317,\"filled-amount\":1.000,\"unfilled-amount\":1.100,\"state\":\"partial-filled\"}]}]";
            rabbitMQTemplate.convertAndSend("tfbee.dev.legend.sub","",p);
            log.info("======第{}推消息======>{}",i,p);
            Thread.sleep(1);
        }
    }


    @RequestMapping(value = "/rabbitmq/ticker",method = RequestMethod.GET)
    public void ticker() throws InterruptedException, IOException {
        Random random = new Random();
        int rTmp = 0;

        JSONObject instruments = new JSONObject();
        instruments.put("btcusdt", BigDecimal.valueOf(26585.7));
        instruments.put("ethusdt", BigDecimal.valueOf(1763.02));
        instruments.put("ltcusdt", BigDecimal.valueOf(78.94));
        instruments.put("eosusdt", BigDecimal.valueOf(0.8710));
        instruments.put("bchusdt", BigDecimal.valueOf(113.64));
        instruments.put("trxusdt", BigDecimal.valueOf(0.06));
        instruments.put("filusdt", BigDecimal.valueOf(4.292));
        instruments.put("linkusdt", BigDecimal.valueOf(6.307));
        instruments.put("dotusdt", BigDecimal.valueOf(5.225));
        instruments.put("xrpusdt", BigDecimal.valueOf(0.4365));
        instruments.put("dogeusdt", BigDecimal.valueOf(0.07135));
        instruments.put("shibusdt", BigDecimal.valueOf(0.000008611));
        instruments.put("adausdt", BigDecimal.valueOf(0.36227));
        instruments.put("maticusdt", BigDecimal.valueOf(0.83177));
        instruments.put("solusdt", BigDecimal.valueOf(20.0835));
        instruments.put("bnbusdt", BigDecimal.valueOf(304.87));
        instruments.put("avaxusdt", BigDecimal.valueOf(14.841));
        instruments.put("manausdt", BigDecimal.valueOf(0.4444));
        instruments.put("axsusdt", BigDecimal.valueOf(6.65085));
        instruments.put("uniusdt", BigDecimal.valueOf(4.9860));
        instruments.put("chzusdt", BigDecimal.valueOf(0.10510));
        instruments.put("apeusdt", BigDecimal.valueOf(3.16650));
        instruments.put("ftmusdt", BigDecimal.valueOf(0.36280));
        instruments.put("dashusdt", BigDecimal.valueOf(39.25));
        instruments.put("atomusdt", BigDecimal.valueOf(10.948));
        instruments.put("arbusdt", BigDecimal.valueOf(1.0762));
        instruments.put("xlmusdt", BigDecimal.valueOf(0.08918));
        instruments.put("suiusdt", BigDecimal.valueOf(1.034));
        instruments.put("pepeusdt", BigDecimal.valueOf(0.0000011971));

        String p;
        long i = 9189338l;
        while (true){
            for (String key:instruments.keySet()) {

                p = "[{\"id\":"+i+",\"symbol\":\""+channel+"."+key+"\",\"ts\":"+System.currentTimeMillis()+",\"publish-ts\":"+System.currentTimeMillis()+",\"order-type\":\"buy-market\",\"items\":[{\"order-id\":"+i+",\"seq-id\":"+i+",\"role\":\"maker\",\"price\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(random.nextGaussian() * Math.sqrt(0.001))))+",\"filled-amount\":0.01,\"state\":\"filled\"},{\"order-id\":"+i+",\"seq-id\":"+i+",\"role\":\"taker\",\"state\":\"filled\",\"price\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(random.nextGaussian() * Math.sqrt(0.001))))+",\"unfilled-amount\":0}]}]";
                rabbitMQTemplate.convertAndSend(ticker,"#."+key,p);
                log.info("======第{}推消息======>{}",i,p);
                Thread.sleep(1);
                i ++;
            }
        }
    }

    @RequestMapping(value = "/rabbitmq/kline1",method = RequestMethod.GET)
    public void kline() throws InterruptedException, IOException {

        JSONObject instruments = new JSONObject();
        instruments.put("btcusdt", BigDecimal.valueOf(18780));
        instruments.put("ethusdt", BigDecimal.valueOf(1258));
        instruments.put("ltcusdt", BigDecimal.valueOf(51));
        instruments.put("bchusdt", BigDecimal.valueOf(111));
        instruments.put("trxusdt", BigDecimal.valueOf(0.059));
        instruments.put("filusdt", BigDecimal.valueOf(5.3));
        instruments.put("linkusdt", BigDecimal.valueOf(6.78));
        instruments.put("dotusdt", BigDecimal.valueOf(7.29));
        instruments.put("xrpusdt", BigDecimal.valueOf(0.418));
        instruments.put("dogeusdt", BigDecimal.valueOf(0.05));
        instruments.put("shibusdt", BigDecimal.valueOf(0.00001053));
        instruments.put("adausdt", BigDecimal.valueOf(0.44));
        instruments.put("maticusdt", BigDecimal.valueOf(0.72));
        instruments.put("solusdt", BigDecimal.valueOf(31.15));
        instruments.put("bnbusdt", BigDecimal.valueOf(264.78));
        instruments.put("avaxusdt", BigDecimal.valueOf(16.747));
        instruments.put("manausdt", BigDecimal.valueOf(0.6972));
        instruments.put("axsusdt", BigDecimal.valueOf(11.87));
        instruments.put("uniusdt", BigDecimal.valueOf(5.46));
        instruments.put("fttusdt", BigDecimal.valueOf(22.65));

        List<String> scale = new ArrayList<String>();
        scale.add("1min");
        scale.add("5min");
        scale.add("15min");
        scale.add("30min");
        scale.add("60min");
        scale.add("1hour");
        scale.add("4hour");
        scale.add("1day");
        scale.add("1week");
        scale.add("1month");

        int num = 1;
        SendKlineNotice sendKlineNotice = new SendKlineNotice(kline,num,instruments,scale,channel,rabbitMQTemplate);
        Thread t1 = new Thread(sendKlineNotice);
        Thread t2 = new Thread(sendKlineNotice);
        Thread t3 = new Thread(sendKlineNotice);
        Thread t4 = new Thread(sendKlineNotice);
        Thread t5 = new Thread(sendKlineNotice);

        t1.start();
//        Thread.sleep(10);
        sendKlineNotice.flag = false;
        t2.start();
        t3.start();
        t4.start();
        t5.start();

//        Random random = new Random();
//        String p;
//        int i = 1;
//        long time;
//        String routingKey ;
//        int rTmp;
//        while (true){
//            for (String key : instruments.keySet()) {
//                for (String s : scale) {
//                        time = System.currentTimeMillis();
//                        rTmp = random.nextInt(100);
//                        p = "{\"status\":\"ok\",\"event_rep\":\"\",\"channel\":\"market_" + channel + "." + key + "_kline_" + s + "\",\"ts\":" + time + ",\"tick\":" +
//                                "{\"id\":" + i + ",\"amount\":" + random.nextInt(100000) + ",\"open\":" + instruments.getBigDecimal(key) + ",\"close\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.02)))) + ",\"high\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.03)))) + ",\"low\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"vol\":" + random.nextInt(1000000) + ",\"mrid\":-1000}}";
//
//                        routingKey = "market_" + channel + "." + key + "_kline_" + s + "_"+i+"_" + time + "_" + time + "_" + time;
//
//                        try {
//                            rabbitMQTemplate.convertAndSend(kline, routingKey, GzipUtil.compress(p));
//
//                            log.info("======第{}推消息=======时间:{}ms======routingKey={}======>{}", i, System.currentTimeMillis()-time,routingKey, p);
////                        Thread.sleep(5000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        i++;
//                }
//            }
//        }

    }

    @RequestMapping(value = "/rabbitmq/kline2",method = RequestMethod.GET)
    public void kline2() throws InterruptedException, IOException {

        JSONObject instruments = new JSONObject();
//        instruments.put("btcusdt", BigDecimal.valueOf(18780));
//        instruments.put("ethusdt", BigDecimal.valueOf(1258));
//        instruments.put("ltcusdt", BigDecimal.valueOf(51));
//        instruments.put("bchusdt", BigDecimal.valueOf(111));
//        instruments.put("trxusdt", BigDecimal.valueOf(0.059));
//        instruments.put("filusdt", BigDecimal.valueOf(5.3));
//        instruments.put("linkusdt", BigDecimal.valueOf(6.78));
//        instruments.put("dotusdt", BigDecimal.valueOf(7.29));
//        instruments.put("xrpusdt", BigDecimal.valueOf(0.418));
//        instruments.put("dogeusdt", BigDecimal.valueOf(0.05));
//        instruments.put("shibusdt", BigDecimal.valueOf(0.00001053));
//        instruments.put("adausdt", BigDecimal.valueOf(0.44));
//        instruments.put("maticusdt", BigDecimal.valueOf(0.72));
//        instruments.put("solusdt", BigDecimal.valueOf(31.15));
//        instruments.put("bnbusdt", BigDecimal.valueOf(264.78));
//        instruments.put("avaxusdt", BigDecimal.valueOf(16.747));
//        instruments.put("manausdt", BigDecimal.valueOf(0.6972));
//        instruments.put("axsusdt", BigDecimal.valueOf(11.87));
        instruments.put("uniusdt", BigDecimal.valueOf(5.46));
//        instruments.put("fttusdt", BigDecimal.valueOf(22.65));

        List<String> scale = new ArrayList<String>();
        scale.add("1min");
        scale.add("5min");
        scale.add("15min");
        scale.add("30min");
        scale.add("60min");
        scale.add("1hour");
        scale.add("4hour");
        scale.add("1day");
        scale.add("1week");
        scale.add("1month");


        Random random = new Random();
        String p;
        int i = 1000000000;
        long time;
        String routingKey ;
        int rTmp;
        while (true){
            for (String key : instruments.keySet()) {
                for (String s : scale) {
                        time = System.currentTimeMillis();
                        rTmp = random.nextInt(100);
                        p = "{\"status\":\"ok\",\"event_rep\":\"\",\"channel\":\"market_" + channel + "." + key + "_kline_" + s + "\",\"ts\":" + time + ",\"tick\":" +
                                "{\"id\":" + i + ",\"amount\":" + random.nextInt(100000) + ",\"open\":" + instruments.getBigDecimal(key) + ",\"close\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.02)))) + ",\"high\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.03)))) + ",\"low\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"vol\":" + random.nextInt(1000000) + ",\"mrid\":-1000}}";

                        routingKey = "market_" + channel + "." + key + "_kline_" + s + "_"+i+"_" + time + "_" + time + "_" + time;

                        try {
                            rabbitMQTemplate.convertAndSend(kline, routingKey, GzipUtil.compress(p));
//                            rabbitMQTemplate.convertAndSend(kline, "", GzipUtil.compress(p));

                            log.info("======第{}推消息=======时间:{}ms======routingKey={}======>{}", i, System.currentTimeMillis()-time,routingKey, p);
                        Thread.sleep(2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        i++;
                        
                }
            }
        }

    }

    @RequestMapping(value = "/rabbitmq/depth",method = RequestMethod.GET)
    public void depth() throws InterruptedException, IOException {
        JSONObject instruments = new JSONObject();
        instruments.put("btcusdt", BigDecimal.valueOf(26585.7));
        instruments.put("ethusdt", BigDecimal.valueOf(1763.02));
        instruments.put("ltcusdt", BigDecimal.valueOf(78.94));
        instruments.put("eosusdt", BigDecimal.valueOf(0.8710));
        instruments.put("bchusdt", BigDecimal.valueOf(113.64));
        instruments.put("trxusdt", BigDecimal.valueOf(0.06));
        instruments.put("filusdt", BigDecimal.valueOf(4.292));
        instruments.put("linkusdt", BigDecimal.valueOf(6.307));
        instruments.put("dotusdt", BigDecimal.valueOf(5.225));
        instruments.put("xrpusdt", BigDecimal.valueOf(0.4365));
        instruments.put("dogeusdt", BigDecimal.valueOf(0.07135));
        instruments.put("shibusdt", BigDecimal.valueOf(0.000008611));
        instruments.put("adausdt", BigDecimal.valueOf(0.36227));
        instruments.put("maticusdt", BigDecimal.valueOf(0.83177));
        instruments.put("solusdt", BigDecimal.valueOf(20.0835));
        instruments.put("bnbusdt", BigDecimal.valueOf(304.87));
        instruments.put("avaxusdt", BigDecimal.valueOf(14.841));
        instruments.put("manausdt", BigDecimal.valueOf(0.4444));
        instruments.put("axsusdt", BigDecimal.valueOf(6.65085));
        instruments.put("uniusdt", BigDecimal.valueOf(4.9860));
        instruments.put("chzusdt", BigDecimal.valueOf(0.10510));
        instruments.put("apeusdt", BigDecimal.valueOf(3.16650));
        instruments.put("ftmusdt", BigDecimal.valueOf(0.36280));
        instruments.put("dashusdt", BigDecimal.valueOf(39.25));
        instruments.put("atomusdt", BigDecimal.valueOf(10.948));
        instruments.put("arbusdt", BigDecimal.valueOf(1.0762));
        instruments.put("xlmusdt", BigDecimal.valueOf(0.08918));
        instruments.put("suiusdt", BigDecimal.valueOf(1.034));
        instruments.put("pepeusdt", BigDecimal.valueOf(0.0000011971));

        int num = 1;
//        SendDepthNotice sendDepthNotice = new SendDepthNotice(depth,num,instruments,channel,rabbitMQTemplate);
//        Thread t1 = new Thread(sendDepthNotice);
//        Thread t2 = new Thread(sendDepthNotice);
//        Thread t3 = new Thread(sendDepthNotice);
//
//        t1.start();
////        Thread.sleep(3);
////        sendDepthNotice.flag = false;
//        t2.start();
//        t3.start();

        Random random = new Random();
        int rTmp ;
        String p;
        int i = 3780;
        String routingKey;
        long time;
        while (true){
            for (String key:instruments.keySet()) {
                rTmp = random.nextInt(100);
                time = System.currentTimeMillis();
                p = "{\"asks\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.05)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.03)))) + "," + random.nextInt(1000) + "]],\"bids\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.02)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + "," + random.nextInt(1000) + "]]}";
                routingKey = "market_" + channel + "." + key + "_depth_step0_" + i + "_" + time;
                rabbitMQTemplate.convertAndSend(depth, routingKey, p);
                log.info("======第{}推消息======routingKey={}======>{}", i, routingKey, p);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i ++;
            }
        }

    }

    @RequestMapping(value = "/rabbitmq/orther",method = RequestMethod.GET)
    public void orther() throws InterruptedException, IOException {
        Random random = new Random();

        JSONObject instruments = new JSONObject();
//        instruments.put("btcusdt", BigDecimal.valueOf(18780));
//        instruments.put("ethusdt", BigDecimal.valueOf(1258));
//        instruments.put("ltcusdt", BigDecimal.valueOf(51));
//        instruments.put("bchusdt", BigDecimal.valueOf(111));
        instruments.put("trxusdt", BigDecimal.valueOf(0.06198));
//        instruments.put("filusdt", BigDecimal.valueOf(5.3));
//        instruments.put("linkusdt", BigDecimal.valueOf(6.78));
//        instruments.put("dotusdt", BigDecimal.valueOf(7.29));
//        instruments.put("xrpusdt", BigDecimal.valueOf(0.418));
//        instruments.put("dogeusdt", BigDecimal.valueOf(0.05));
//        instruments.put("shibusdt", BigDecimal.valueOf(0.00001053));
//        instruments.put("adausdt", BigDecimal.valueOf(0.44));
//        instruments.put("maticusdt", BigDecimal.valueOf(0.72));
//        instruments.put("solusdt", BigDecimal.valueOf(31.15));
//        instruments.put("bnbusdt", BigDecimal.valueOf(264.78));
//        instruments.put("avaxusdt", BigDecimal.valueOf(16.747));
//        instruments.put("manausdt", BigDecimal.valueOf(0.6972));
//        instruments.put("axsusdt", BigDecimal.valueOf(11.87));
//        instruments.put("uniusdt", BigDecimal.valueOf(5.46));
//        instruments.put("fttusdt", BigDecimal.valueOf(22.65));




        Thread t1 = new Thread(new Runnable() {
            int i = 0;
            long time ;
            String indexPriceRoutingKey ;
            String markPriceRoutingKey ;
            String fundingrateRoutingKey ;
            String spotfillsRoutingKey ;

            String indexPriceP;
            String markPriceP;
            String fundingrateP;
            String spotfillsP;
            int rTmp = 0;

            @Override
            public void run() {
                while (true){
                    rTmp = random.nextInt(1000);
                    for (String key:instruments.keySet()) {
                        time = System.currentTimeMillis();
                        rTmp = random.nextInt(100);

                        //标记价格
                        markPriceP = "{\"mp\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01))))+",\"ts\":"+time+"}";
                        markPriceRoutingKey = "market_"+key+"_mark_price_"+time;
                        rabbitMQTemplate.convertAndSend(markPrice,markPriceRoutingKey,markPriceP);

                        //指数价格
//                        indexPriceP = "{\"ip\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01))))+",\"ts\":"+time+"}";
//                        indexPriceRoutingKey = "market_"+key+"_index_price_"+time;
//                        rabbitMQTemplate.convertAndSend(indexPrice,indexPriceRoutingKey,indexPriceP);

                        //资金费率
//                        fundingrateP = "{\"fr\":"+BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.00001))+",\"ts\":"+time+"}";
//                        fundingrateRoutingKey = "market_"+key+"_funding_rate_"+time;
//                        rabbitMQTemplate.convertAndSend(fundingrate,fundingrateRoutingKey,fundingrateP);

//                //闪电合约最新成交 交互参数
//                spotfillsP = "{\"id\":"+i+",\"side\":\"buy\",\"price\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01))))+","+random.nextInt(1000)+",\"vol\":"+random.nextInt(10000)+",\"amount\":"+random.nextInt(1000000)+",\"ts\":"+time+"}";
//                spotfillsRoutingKey = "market_GjFt."+key+"_spot_fills_"+time;
//                rabbitMQTemplate.convertAndSend(spotfills,spotfillsRoutingKey,spotfillsP);


                        log.info("======第{}推消息======标记价格======routingKey={}======>{}",i,markPriceRoutingKey,markPriceP);
//                        log.info("======第{}推消息======指数价格======routingKey={}======>{}",i,indexPriceRoutingKey,indexPriceP);
//                        log.info("======第{}推消息======资金费率======routingKey={}======>{}",i,fundingrateRoutingKey,fundingrateP);
//                log.info("======第{}推消息======闪电合约最新成交 交互参数======routingKey={}======>{}",i,spotfillsRoutingKey,spotfillsP);
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i ++;
                    }
                }
            }
        });
        t1.start();

    }

    @RequestMapping(value = "/rabbitmq/marketPrice",method = RequestMethod.GET)
    public void marketPrice(@RequestParam("symbol") String symbol,@RequestParam("price") BigDecimal price)  {
        long time = System.currentTimeMillis();
        String markPriceP = "{\"mp\":"+price+",\"ts\":"+time+"}";
        String markPriceRoutingKey = "market_"+symbol+"_mark_price_"+time;
        rabbitMQTemplate.convertAndSend(markPrice,markPriceRoutingKey,markPriceP);
        log.info("======标记价格======routingKey={}======>{}",markPriceRoutingKey,markPriceP);
    }
}
