package com.zmj.demo.client;

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
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
public class ReConnectWSClient {

    private WebSocketClient webSocketClient;

    private URI serverUri;
    public ReConnectWSClient(URI serverUri){
        this.serverUri = serverUri;
        init();
    }

    public void init(){
       createWSClient();
    }
    private void needReconect(){
        createWSClient();
    }
    private void createWSClient(){
        webSocketClient = new WebSocketClient(serverUri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_mark_price\",\"cb_id\":\"10001\"}}");
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
                log.info("----re---- ByteBuffer接收到服务端数据：{} --------" , res);
                if (res.contains("ping")) {
                    String time = JSONObject.parseObject(res).getString("ping");
                    webSocketClient.send("{\"pong\": \"" + time + "\"}");
                    log.info("发送：：：{\"pong\":\"{}\"}",time);
                }
            }

            @Override
            public void onMessage(String arg0) {
                log.info("----re---- String接收到服务端数据： {} --------" , arg0 );
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                needReconect();
            }

            @Override
            public void onError(Exception e) {

            }
        };
    }
    public void connect(){
        webSocketClient.connect();
    }
}
