package com.zmj.demo.wsClient;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import com.zmj.demo.dao.dev.OptionsInfoDao;
import com.zmj.demo.dao.dev.SmsEmailCode;
import com.zmj.demo.dao.test.TfbeeKline;
import com.zmj.demo.domain.dev1.OptionsInfoChain;
import com.zmj.demo.service.impl.plugin.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.net.ssl.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j

public class ReConnectWSClient {

    @Autowired
    private RedisService redisService;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private TfbeeKline tfbeeKline;

    /**
     * 字符串消息回调
     */
    private Consumer<String> msgStr;
    /**
     * 字节流消息回调
     */
    private Consumer<ByteBuffer> msgByte;
    /**
     * 异常回调
     */
    private Consumer<Exception> error;
    /**
     * 连接标识
     */
    private String key;
    /**
     * ws服务端连接
     */
    private URI serverUri;
    /**
     * 尝试重连标识
     */
    private AtomicBoolean tryReconnect;
    /**
     * 需要ping标识
     */
    private AtomicBoolean needPing;
    /**
     * websocket连接实体
     */
    private WebSocketClient webSocketClient;
    /**
     * 重连次数
     */
    private AtomicInteger reConnectTimes;
    /**
     * 连接结束标识
     */
    private AtomicBoolean end;
    /**
     * 连接后初始发送报文，这里也可以不需要，如果服务端主动断开连接，重连后可以继续推送报文的话。
     */
    private String initReConnectReq;
    /**
     * 结束回调
     */
    private Consumer<String> endConsumer;
    /**
     * 配置重连次数
     */
    private int reConnectNum;

    /**
     * 期权币种列表
     */
    private List<OptionsInfoChain> optionsInfoChains;

    public ReConnectWSClient(
            URI serverUri,
            String key,
            int reConnectNum,
            Consumer<String> msgStr,
            Consumer<ByteBuffer> msgByte,
            Consumer<Exception> error,
            List<OptionsInfoChain> optionsInfoChains) {
        this.msgStr = msgStr;
        this.msgByte = msgByte;
        this.error = error;
        this.key = key;
        this.reConnectNum = reConnectNum;
        this.serverUri = serverUri;
        this.tryReconnect = new AtomicBoolean(false);
        this.needPing = new AtomicBoolean(true);
        this.reConnectTimes = new AtomicInteger(0);
        this.end = new AtomicBoolean(false);
        this.endConsumer = this::close;
        this.optionsInfoChains = optionsInfoChains;
        init();
    }

    /**
     * 初始化连接
     */
    public void init() {
        // 创建连接
        createWebSocketClient();
        // ping线程
        circlePing();
    }

    private void needReconnect() throws Exception {
        Thread.sleep(1000);
        int cul = reConnectTimes.incrementAndGet();
        if (cul > reConnectNum) {
            close("real stop");
            throw new Exception("服务端断连，"+reConnectNum+"次重连均失败");
        }
        log.warn("[{}]第[{}]次断开重连", key, cul);
        if (tryReconnect.get()) {
            log.error("[{}]第[{}]次断开重连结果 -> 连接正在重连，本次重连请求放弃", key, cul);
            needReconnect();
            return;
        }
        try {
            tryReconnect.set(true);

            if (webSocketClient.isOpen()) {
                log.warn("[{}]第[{}]次断开重连，关闭旧连接", key, cul);
                webSocketClient.closeConnection(2, "reconnect stop");
            }
            webSocketClient = null;
            createWebSocketClient();
            connect();
//            if (StrUtil.hasBlank(initReConnectReq)) {
//                send(initReConnectReq);
//            }
        } catch (Exception exception) {
            log.error("[{}]第[{}]次断开重连结果 -> 连接正在重连，重连异常:[{}]", key, cul, exception.getMessage());
            needReconnect();
        } finally {
            tryReconnect.set(false);
        }
    }

    private void createWebSocketClient() {
        webSocketClient =
                new WebSocketClient(serverUri) {
                    @Override
                    public void onOpen(ServerHandshake serverHandshake) {
                        log.info("[{}]ReConnectWSClient [onOpen]连接成功{}", key, getRemoteSocketAddress());
                        tryReconnect.set(false);
                        //ticker
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_ticker\"}}");

                        //k线
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_xxOi.BTC-230316-24600-C_kline_1min\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ty9f.btcusdt_kline_1min\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_awgf.btcusdt_depth_step0\",\"cb_id\":\"10001001\"}}");

                        //历史K线
//        webSocketClient.send("{\"event\":\"req\",\"params\":{\"channel\":\"market_xxOi.btcusdt_kline_60min\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"req\",\"params\":{\"channel\":\"market_GjFt.btcusdt_kline_60min\",\"cb_id\":\"10001\"}}");

                        //全部行情
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"review\"}}");
//            webSocketClient.send("{\"event\":\"sub\",\"params\":{\"cb_id\":\"iOS_qoz6_1660123161355_lPEUSYZciR\",\"channel\":\"market_qoz6.btcusdt_kline_5min\"}}");


                        //标记价格
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ethusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ltcusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_eosusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_bchusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_trxusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_filusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_linkusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_dotusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_xrpusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_dogeusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_shibusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_adausdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_maticusdt_mark_price\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_solusdt_mark_price\",\"cb_id\":\"10001\"}}");

          //指数价格
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_index_price\",\"cb_id\":\"10001\"}}");


        // hpx模拟
        String channel = "qoz6";
        // 币王模拟
//        String channel = "Mt4y";
//                        String[] instruments = {"btcusdt","ethusdt", "ltcusdt", "eosusdt", "bchusdt", "trxusdt", "filusdt", "linkusdt", "dotusdt", "xrpusdt","dogeusdt","shibusdt", "adausdt", "maticusdt", "solusdt", "bnbusdt", "avaxusdt", "lunausdt", "manausdt", "axsusdt", "uniusdt", "fttusdt", "chzusdt", "apeusdt", "ftmusdt", "blkusdt", "dashusdt", "atomusdt"};
        //最新成交价market_qoz6.btcusdt_ticker
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".btcusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".ethusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".ltcusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".eosusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_G"+channel+".bchusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".trxusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".filusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".linkusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".dotusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".xrpusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".dogeusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".shibusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".adausdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".matiusdt_ticker\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+".solusdt_ticker\",\"cb_id\":\"10001\"}}");
                        //市场深度行情数据
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_xxOi.BTC-230314-20300-C_depth_step0\",\"cb_id\":\"10001\",\"asks\":150,\"bids\":150}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ty9f.btcusdt_depth_step0\",\"cb_id\":\"10001\",\"asks\":150,\"bids\":150}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"asks\":20,\"channel\":\"market_FaJ6.dogeusdt_depth_step0\",\"bids\":20,\"cb_id\":\"Android_FaJ6\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"asks\":20,\"channel\":\"market_FaJ6.trxusdt_depth_step0\",\"bids\":20,\"cb_id\":\"Android_FaJ6\"}}");
                        //资金费率
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_funding_rate\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ethusdt_funding_rate\",\"cb_id\":\"10001\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ltcusdt_funding_rate\",\"cb_id\":\"10001\"}}");

//        web最新成交
//        webSocketClient.send("{\"event\":\"req\",\"params\":{\"channel\":\"market_GjFt.ethusdt_spot_fills\",\"cb_id\":\"10001000\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_ty9f.ethusdt_spot_fills\",\"cb_id\":\"10001000\"}}");
//        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_xxOi.btcusdt_ticker\",\"cb_id\":\"Android_qoz6\"}}");

          // 期权实时成交
//      {"event":"sub","params":{"top":20,"channel":"market_xxOi.BTC-230314-20700-C_trade_ticker","cb_id":"Android_xxOi_saas_1.0.3"}}
          // 期权的标记价格
//      {"event":"sub","params":{"channel":"market_xxOi.BTC-230314-20400-C_ticker","cb_id":"Android_xxOi_saas_1.0.3"}}
          // 期权盘口
//      webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_xxOi.BTC-230314-20300-C_depth_step0\",\"cb_id\":\"10001\",\"asks\":150,\"bids\":150}}");
          //期权指数价格
//       webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_index_price\",\"cb_id\":\"10001\"}}");

                        for (OptionsInfoChain oic: optionsInfoChains) {
                            webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+"."+oic.getInstrumentName()+"_ticker\",\"cb_id\":\"Android_xxOi_saas_1.0.3\"}}");
                            webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_"+channel+"."+oic.getInstrumentName()+"_depth_step0\",\"cb_id\":\"10001\",\"asks\":150,\"bids\":150}}");
                        }
                        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_index_price\",\"cb_id\":\"10001\"}}");
                    }

                    @Override
                    public void onMessage(String text) {
                        log.info("[{}]ReConnectWSClient [onMessage-String]接收到服务端数据：text={}", key, text);
                        msgStr.accept(text);
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
//                        log.info("-------- ByteBuffer接收到服务端数据：{} --------" , res);
//                        log.info("[{}]ReConnectWSClient [onMessage-ByteBuffer]接收到服务端数据：{}", key, res);
                        if (res.contains("ping")) {
                            String time = JSONObject.parseObject(res).getString("ping");
                            webSocketClient.send("{\"pong\": \"" + time + "\"}");
//                            log.info("发送：：：{\"pong\":\"{}\"}",time);
                        }

                        if(res.contains("market_") && res.contains("usdt_kline_") && res.contains("tick")){
                            JSONObject tick = JSONObject.parseObject(res).getJSONObject("tick");
                            System.out.println(tick.getBigDecimal("open").setScale(3, BigDecimal.ROUND_UP)+"======="+tick.getBigDecimal("close").setScale(3, BigDecimal.ROUND_UP)+"======="+tick.getBigDecimal("high").setScale(3, BigDecimal.ROUND_UP)+"======="+tick.getBigDecimal("low").setScale(3, BigDecimal.ROUND_UP));
                        }
                        if (res.contains("market_") && res.contains("usdt_mark_price")){
                            try {
                                if (JSONObject.parseObject(res).getJSONObject("data") != null) {
                                    String mp = JSONObject.parseObject(res).getJSONObject("data").getString("mp");
                                    String channel = JSONObject.parseObject(res).getString("channel");
                                    String symbol = channel.split("_")[1].substring(0,channel.split("_")[1].length()-4);
                                    redisService.set(symbol, mp);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (res.contains("market_") && res.contains("usdt_ticker")){
                            try {
                                if (JSONObject.parseObject(res).getJSONObject("tick") != null) {
                                    String mp = JSONObject.parseObject(res).getJSONObject("tick").getString("close");
                                    String channel = JSONObject.parseObject(res).getString("channel");
                                    String symbol = channel.split("_")[1].substring(5,channel.split("_")[1].length()-4);
                                    redisService.set(symbol, mp);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // 期权盘口
                        if (res.contains("market_") && res.contains("depth_step0")){
                            try {
                                if (JSONObject.parseObject(res).getJSONObject("tick") != null) {
                                    String channel = JSONObject.parseObject(res).getString("channel");
                                    String symbol = channel.split("\\.")[1];
                                    redisService.set("options::depth_step0::"+symbol, JSONObject.parseObject(res).getJSONObject("tick").toJSONString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // 期权标记价格
                        if (res.contains("market_") && res.contains("_ticker")){
                            try {
                                if (JSONObject.parseObject(res).getJSONObject("tick") != null) {
                                    String channel = JSONObject.parseObject(res).getString("channel");
                                    String symbol = channel.split("\\.")[1];
                                    redisService.set("options::ticker::"+symbol, JSONObject.parseObject(res).getJSONObject("tick").toJSONString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // 合约指数价格
                        if (res.contains("market_") && res.contains("_index_price")){
                            try {
                                if (JSONObject.parseObject(res).getJSONObject("data") != null) {
                                    String channel = JSONObject.parseObject(res).getString("channel");
                                    String symbol = channel.split("_")[1];
                                    redisService.set("contract::index_price::"+symbol, JSONObject.parseObject(res).getJSONObject("data").toJSONString());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onWebsocketPong(WebSocket conn, Framedata f) {
//                        log.info(
//                                "[{}]ReConnectWSClient [onWebsocketPong]接收到服务端数据：opcode={}",
//                                key,
//                                f.getOpcode());
                    }

                    @Override
                    public void onClose(int i, String s, boolean b) {
                        log.info("[{}]ReConnectWSClient [onClose]关闭，s={}，b={}", key, s, b);
                        if (s.contains("real")) {
                            if (end.get()) {
                                return;
                            }
                        }
                        try {
                            needReconnect();
                        } catch (Exception exception) {
                            endConsumer.accept("reconnect error");
                            error.accept(exception);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        log.info("[{}]ReConnectWSClient [onError]异常，e={}", key, e);
                        endConsumer.accept("error close");
                        error.accept(e);
                    }
                };

        // 开启代理后，下方的代码需要注释
        if (serverUri.toString().contains("wss://")) {
            trustAllHosts(webSocketClient);
        }
    }

    public void circlePing() {
        new Thread(
                () -> {
                    while (needPing.get()) {
                        if (webSocketClient.isOpen()) {
                            webSocketClient.sendPing();
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    log.warn("[{}]Ping循环关闭", key);
                })
                .start();
    }

    /**
     * 连接
     *
     * @throws Exception 异常
     */
    public void connect() throws Exception {
        webSocketClient.connectBlocking(10, TimeUnit.SECONDS);
    }

    /**
     * 发送
     *
     * @param msg 消息
     * @throws Exception 异常
     */
    public void send(String msg) throws Exception {
        this.initReConnectReq = msg;
        if (webSocketClient.isOpen()) {
            webSocketClient.send(msg);
        }
    }

    /**
     * 关闭
     *
     * @param msg 关闭消息
     */
    public void close(String msg) {
        needPing.set(false);
        end.set(true);
        if (webSocketClient != null) {
            webSocketClient.closeConnection(3, msg);
        }
    }

    /**
     * 忽略证书
     *
     * @param client
     */
    public void trustAllHosts(WebSocketClient client) {
        TrustManager[] trustAllCerts =
                new TrustManager[]{
                        new X509ExtendedTrustManager() {

                            @Override
                            public void checkClientTrusted(
                                    X509Certificate[] x509Certificates, String s, Socket socket)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(
                                    X509Certificate[] x509Certificates, String s, Socket socket)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkClientTrusted(
                                    X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(
                                    X509Certificate[] x509Certificates, String s, SSLEngine sslEngine)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                                    throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                                    throws CertificateException {
                            }

                            @Override
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        }
                };

        try {
            SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory socketFactory = ssl.getSocketFactory();
            client.setSocketFactory(socketFactory);
        } catch (Exception e) {
            log.error("ReConnectWSClient trustAllHosts 异常，e={0}", e);
        }
    }
}

