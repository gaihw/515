package com.zmj.demo.client;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import com.zmj.demo.dao.dev1.TfbeeKline;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class WebSocketClient extends org.java_websocket.client.WebSocketClient {

    public WebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Autowired
    private org.java_websocket.client.WebSocketClient webSocketClient;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TfbeeKline tfbeeKline;

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);

    @Override
    public void onOpen(ServerHandshake arg0) {
        log.info("------ MyWebSocket onOpen ------");
        //ticker
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_ticker\"}}");

        //k线
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_qoz6.btcusdt_kline_1min\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ltcusdt_kline_5min\",\"cb_id\":\"10001\"}}");

        //历史K线
//        webSocketClient.send("{\"event\":\"req\",\"params\":{\"channel\":\"market_btcusdt_kline_1min\",\"cb_id\":\"10001\"}}");

        //全部行情
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"review\"}}");


        //标记价格
        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_mark_price\",\"cb_id\":\"10001\"}}");
        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ethusdt_mark_price\",\"cb_id\":\"10001\"}}");
        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ltcusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ltcusdt_mark_price\",\"cb_id\":\"10001\"}}");

        //指数价格
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_eosusdt_index_price\",\"cb_id\":\"10001\"}}");

        //市场深度行情数据
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_qoz6.btcusdt_depth_step0\",\"cb_id\":\"10001\",\"asks\":150,\"bids\":150}}");

        //资金费率
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_funding_rate\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ethusdt_funding_rate\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ltcusdt_funding_rate\",\"cb_id\":\"10001\"}}");

//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_qoz6.btcusdt_ticker\",\"cb_id\":\"Android_qoz6\"}}");

        //测试环境
//        scheduledThreadPool.scheduleAtFixedRate(() -> {
//            System.out.println("发送：：：{\"event\":\"ping\"}");
//            webSocketClient.send("{\"event\":\"ping\"}");
//                    }, 0, 5, TimeUnit.SECONDS);

//        webSocketClient.send("{\"event\":\"subscribe\",\"params\":{\"biz\":\"mark_price\",\"type\":\"mark_price\",\"base\":\"btc\",\"channel\":\"subscribe\",\"quote\":\"usd\",\"zip\":false}}");
    }


    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        log.info("------ MyWebSocket onClose ------{}", arg1);
    }

    @Override
    public void onError(Exception arg0) {
        log.info("------ MyWebSocket onError ------{}", arg0);
    }

    @Override
    public void onMessage(ByteBuffer buffer) {
        //获取buffer中有效大小
        int len = buffer.limit() - buffer.position();
        byte[] bytes = new byte[len];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = buffer.get();
        }
        String res = GzipUtil.uncompressToString(bytes);
        log.info("-------- ByteBuffer接收到服务端数据：{} --------" , res);

        if (res.contains("ping")) {
            String time = JSONObject.parseObject(res).getString("ping");
            webSocketClient.send("{\"pong\": \"" + time + "\"}");
            log.info("发送：：：{\"pong\":\"{}\"}",time);
        }

        if (res.contains("market_btcusdt_depth_step0")){
            try {
                if (JSONObject.parseObject(res).getJSONObject("tick") != null) {
                    String bids_0 = JSONObject.parseObject(res).getJSONObject("tick").getJSONArray("bids").getJSONArray(0).getString(0);
                    redisService.addValue("btc", bids_0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (res.contains("market_btcusdt_mark_price")){
            try {
                if (JSONObject.parseObject(res).getJSONObject("data") != null) {
                    String mp = JSONObject.parseObject(res).getJSONObject("data").getString("mp");
                    redisService.addValue("btc", mp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (res.contains("market_ethusdt_mark_price")){
            try {
                if (JSONObject.parseObject(res).getJSONObject("data") != null) {
                    String mp = JSONObject.parseObject(res).getJSONObject("data").getString("mp");
                    redisService.addValue("eth", mp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (res.contains("market_ltcusdt_mark_price")){
            try {
                if (JSONObject.parseObject(res).getJSONObject("data") != null) {
                    String mp = JSONObject.parseObject(res).getJSONObject("data").getString("mp");
                    redisService.addValue("ltc", mp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMessage(String arg0) {
        log.info("-------- String接收到服务端数据： {} --------" , arg0 );
        if (!arg0.contains("event")){
            JSONObject jsonObject = JSONObject.parseObject(arg0);
            System.out.println(jsonObject.getString("base")+"==="+jsonObject.getJSONObject("data").getBigDecimal("p"));
            try {
                redisService.addValue(jsonObject.getString("base"),jsonObject.getJSONObject("data").getString("p"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
