package com.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dao.dev1.TfbeeKline;
import com.domain.Market1minBtcusdt;
import com.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class WebSocketController {

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private TfbeeKline tfbeeKline;

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/subscribe",method = RequestMethod.GET)
    public List<Market1minBtcusdt> subscribe() {
        log.info("数据库返回：：：：{}",tfbeeKline.getMarket1minBtcUsdt("1642730220"));
        return tfbeeKline.getMarket1minBtcUsdt("1642730220");
    }
    @RequestMapping(value = "/test/market/btcusdt/ticker",method = RequestMethod.GET)
    public void marketBtcusdtTicker() {
        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_ticker\"}}");

    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @CrossOrigin
    public String test() {
        return "{\"name\":\"网站\",\"num\":3,\"sites\":[{\"name\":\"Google\",\"info\":[\"Android\",\"Google 搜索\",\"Google 翻译\"]},{\"name\":\"Runoob\",\"info\":[\"菜鸟教程\",\"菜鸟工具\",\"菜鸟微信\"]},{\"name\":\"Taobao\",\"info\":[\"淘宝\",\"网购\"]}]}";

    }

    @RequestMapping(value = "/calc",method = RequestMethod.GET)
    public String calc(){
        String res = null;
        try {
            res = String.valueOf(redisService.getValue("market_btcusdt_kline_1min"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray data = JSONObject.parseObject(res).getJSONArray("data");
        List<Market1minBtcusdt> l = null;
        for (int i = 0 ; i < data.size() ; i++) {
            l = tfbeeKline.getMarket1minBtcUsdt(data.getJSONObject(i).getString("id"));
            log.info("数据库：idx:{},数据:{}",data.getJSONObject(i).getString("id"),l);
            if (l.get(0).getAmount().compareTo(data.getJSONObject(i).getBigDecimal("amount")) == 0
                    &&l.get(0).getOpen().compareTo(data.getJSONObject(i).getBigDecimal("open")) == 0
                    &&l.get(0).getClose().compareTo(data.getJSONObject(i).getBigDecimal("close")) == 0
                    &&l.get(0).getHigh().compareTo(data.getJSONObject(i).getBigDecimal("high")) == 0
                    &&l.get(0).getLow().compareTo(data.getJSONObject(i).getBigDecimal("low")) == 0
                    &&l.get(0).getVol().compareTo(data.getJSONObject(i).getBigDecimal("vol")) == 0){
                log.info("K线数据正确：{}",data.getJSONObject(i).getString("id"));
            }
        }
        return "比对完成!";
    }
}
