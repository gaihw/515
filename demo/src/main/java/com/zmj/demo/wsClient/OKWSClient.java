package com.zmj.demo.wsClient;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import com.zmj.demo.dao.test.TfbeeKline;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OKWSClient extends WebSocketClient {

    private URI serverUri;

    public OKWSClient(URI serverUri) {
        super(serverUri);
        this.serverUri = serverUri;
    }

    @Autowired
    private WebSocketClient webSocketClient;

    @Autowired
    private RedisService redisService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private TfbeeKline tfbeeKline;

    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);


    @Override
    public void onOpen(ServerHandshake arg0) {
//        log.info("------ MyWebSocket onOpen ------");
//        String[] instruments = {"btcusdt","ethusdt", "ltcusdt", "eosusdt", "bchusdt", "trxusdt", "filusdt", "linkusdt", "dotusdt", "xrpusdt","dogeusdt","shibusdt", "adausdt", "maticusdt", "solusdt", "bnbusdt", "avaxusdt", "lunausdt", "manausdt", "axsusdt", "uniusdt", "fttusdt", "chzusdt", "apeusdt", "ftmusdt", "blkusdt", "dashusdt", "atomusdt"};
//        webSocketClient.send("{\"op\":\"subscribe\",\"args\":[{\"channel\":\"bbo-tbt\",\"instId\":\"TRX-USDT\"}]}");
//        scheduledThreadPool.scheduleAtFixedRate(() -> {
//            webSocketClient.send("ping");
//        }, 0, 20, TimeUnit.SECONDS);
//        webSocketClient.send("{\"jsonrpc\":\"2.0\",\"method\":\"public/subscribe\",\"id\":42,\"params\":{\"channels\":[\"deribit_volatility_index.btc_usd\"]}}");
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
        log.info("-------- ok接收到服务端数据：{} --------" , res);


    }

    @Override
    public void onMessage(String arg0) {
        log.info("-------- ok接收到服务端数据： {} --------" , arg0 );
        if (arg0.contains("data")){
            JSONArray data = JSONObject.parseObject(arg0).getJSONArray("data");
            // 卖盘
            JSONArray asks = data.getJSONObject(0).getJSONArray("asks");
            // 买盘
            JSONArray bids = data.getJSONObject(0).getJSONArray("bids");
            Books.okAsks = asks.getJSONArray(0).getBigDecimal(0);
            Books.okBids = bids.getJSONArray(0).getBigDecimal(0);
//            if (asks.size() > 0){
//                Books.okAsks = asks.getJSONArray(0).getBigDecimal(0);
//            }
//            if (bids.size() > 0){
//                Books.okBids = bids.getJSONArray(0).getBigDecimal(0);
//            }
            Books.time = data.getJSONObject(0).getLong("ts")/1000;
//            log.info("ok时间:{},卖盘:{},买盘:{}",Books.time,Books.okAsks,Books.okBids);
        }
    }
}
