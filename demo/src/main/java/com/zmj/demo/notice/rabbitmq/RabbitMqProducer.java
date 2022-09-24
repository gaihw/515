package com.zmj.demo.notice.rabbitmq;


import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/rabbitmq/externalTicker",method = RequestMethod.GET)
    public void externalTicker() throws InterruptedException, IOException {

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
        while (true){
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

    @RequestMapping(value = "/rabbitmq/ticker",method = RequestMethod.GET)
    public void ticker() throws InterruptedException, IOException {
        Random random = new Random();
        int rTmp = 0;

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

        String p;
        int i = 1001075486;
        while (true){
            for (String key:instruments.keySet()) {
                p = "[{\"id\":"+i+",\"symbol\":\""+channel+"."+key+"\",\"ts\":"+System.currentTimeMillis()+",\"publish-ts\":"+System.currentTimeMillis()+",\"order-type\":\"buy-market\",\"items\":[{\"order-id\":"+i+",\"seq-id\":"+i+",\"role\":\"maker\",\"price\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01))))+",\"filled-amount\":0.01,\"state\":\"filled\"},{\"order-id\":"+i+",\"seq-id\":"+i+",\"role\":\"taker\",\"state\":\"filled\",\"price\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01))))+",\"unfilled-amount\":0}]}]";
                rabbitMQTemplate.convertAndSend(ticker,"",p);
                log.info("======第{}推消息======>{}",i,p);
                i ++;
            }
        }
    }

    @RequestMapping(value = "/rabbitmq/kline",method = RequestMethod.GET)
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

        int num = 110109;
        SendKlineNotice sendKlineNotice = new SendKlineNotice(kline,num,instruments,scale,channel,rabbitMQTemplate);
        Thread t1 = new Thread(sendKlineNotice);
        Thread t2 = new Thread(sendKlineNotice);
        Thread t3 = new Thread(sendKlineNotice);

        t1.start();
        Thread.sleep(10);
        sendKlineNotice.flag = false;
        t2.start();
        t3.start();

//        String p;
//        int i = 2100486997;
//        long time;
//        String routingKey ;
//        int rTmp;
//        while (true){
//            for (String key : instruments.keySet()) {
//                for (String s : scale) {
//                    executorService.execute(() -> {
//                        String p;
//                        int i = 2100486997;
//                        long time;
//                        String routingKey ;
//                        int rTmp;
//                        time = System.currentTimeMillis();
//                        rTmp = random.nextInt(100);
//                        p = "{\"status\":\"ok\",\"event_rep\":\"\",\"channel\":\"market_" + channel + "." + key + "_kline_" + s + "\",\"ts\":" + time + ",\"tick\":" +
//                                "{\"id\":" + i + ",\"amount\":" + random.nextInt(100000) + ",\"open\":" + instruments.getBigDecimal(key) + ",\"close\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"high\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"low\":" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + ",\"vol\":" + random.nextInt(1000000) + ",\"mrid\":-1000}}";
//
//                        routingKey = "market_" + channel + "." + key + "_kline_" + s + "_"+i+"_" + time + "_" + time + "_" + time;
//
//                        try {
//                            rabbitMQTemplate.convertAndSend(kline, routingKey, GzipUtil.compress(p));
//
//                            log.info("======第{}推消息=======时间:{}ms======routingKey={}======>{}", i, System.currentTimeMillis()-time,routingKey, p);
//                        Thread.sleep(5000);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        i++;
////                        countDownLatch.countDown();
//                    });
//                }
//            }
//        }

    }

    @RequestMapping(value = "/rabbitmq/depth",method = RequestMethod.GET)
    public void depth() throws InterruptedException, IOException {
        Random random = new Random();
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


        Thread t1 = new Thread(new Runnable() {
            int rTmp ;
            String p;
            int i = 50985;
            String routingKey;
            long time;
            @Override
            public void run() {
                while (true){
                    for (String key:instruments.keySet()) {
                        rTmp = random.nextInt(100);
                        time = System.currentTimeMillis();
                        p = "{\"asks\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.05)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.03)))) + "," + random.nextInt(1000) + "]],\"bids\":[[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.02)))) + "," + random.nextInt(1000) + "],[" + instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01)))) + "," + random.nextInt(1000) + "]]}";
                        routingKey = "market_" + channel + "." + key + "_depth_step0_" + i + "_" + time;
                        rabbitMQTemplate.convertAndSend(depth, routingKey, p);
                        log.info("======第{}推消息======routingKey={}======>{}", i, routingKey, p);
                        try {
                            Thread.sleep(3);
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

    @RequestMapping(value = "/rabbitmq/orther",method = RequestMethod.GET)
    public void orther() throws InterruptedException, IOException {
        Random random = new Random();

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
                        indexPriceP = "{\"ip\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01))))+",\"ts\":"+time+"}";
                        indexPriceRoutingKey = "market_"+key+"_index_price_"+time;
                        rabbitMQTemplate.convertAndSend(indexPrice,indexPriceRoutingKey,indexPriceP);

                        //资金费率
                        fundingrateP = "{\"fr\":"+BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.00001))+",\"ts\":"+time+"}";
                        fundingrateRoutingKey = "market_"+key+"_funding_rate_"+time;
                        rabbitMQTemplate.convertAndSend(fundingrate,fundingrateRoutingKey,fundingrateP);

//                //闪电合约最新成交 交互参数
//                spotfillsP = "{\"id\":"+i+",\"side\":\"buy\",\"price\":"+instruments.getBigDecimal(key).multiply(BigDecimal.ONE.add(BigDecimal.valueOf(rTmp).multiply(BigDecimal.valueOf(0.01))))+","+random.nextInt(1000)+",\"vol\":"+random.nextInt(10000)+",\"amount\":"+random.nextInt(1000000)+",\"ts\":"+time+"}";
//                spotfillsRoutingKey = "market_GjFt."+key+"_spot_fills_"+time;
//                rabbitMQTemplate.convertAndSend(spotfills,spotfillsRoutingKey,spotfillsP);


                        log.info("======第{}推消息======标记价格======routingKey={}======>{}",i,markPriceRoutingKey,markPriceP);
                        log.info("======第{}推消息======指数价格======routingKey={}======>{}",i,indexPriceRoutingKey,indexPriceP);
                        log.info("======第{}推消息======资金费率======routingKey={}======>{}",i,fundingrateRoutingKey,fundingrateP);
//                log.info("======第{}推消息======闪电合约最新成交 交互参数======routingKey={}======>{}",i,spotfillsRoutingKey,spotfillsP);
                        try {
                            Thread.sleep(1000);
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
}
