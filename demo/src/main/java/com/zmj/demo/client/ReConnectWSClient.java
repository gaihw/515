package com.zmj.demo.client;

import com.alibaba.fastjson.JSONObject;
import com.zmj.demo.common.GzipUtil;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.*;
import java.net.Socket;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class ReConnectWSClient {
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

    public ReConnectWSClient(
            URI serverUri,
            String key,
            Consumer<String> msgStr,
            Consumer<ByteBuffer> msgByte,
            Consumer<Exception> error) {
        this.msgStr = msgStr;
        this.msgByte = msgByte;
        this.error = error;
        this.key = key;
        this.serverUri = serverUri;
        this.tryReconnect = new AtomicBoolean(false);
        this.needPing = new AtomicBoolean(true);
        this.reConnectTimes = new AtomicInteger(0);
        this.end = new AtomicBoolean(false);
        this.endConsumer = this::close;
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
        if (cul > 3) {
            close("real stop");
            throw new Exception("服务端断连，3次重连均失败");
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
                        webSocketClient.send("{\"event\":\"sub\",\"params\":{\"channel\":\"market_btcusdt_mark_price\",\"cb_id\":\"10001\"}}");
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
                        log.info("[{}]ReConnectWSClient [onMessage-ByteBuffer]接收到服务端数据：{}", key, res);
                        if (res.contains("ping")) {
                            String time = JSONObject.parseObject(res).getString("ping");
                            webSocketClient.send("{\"pong\": \"" + time + "\"}");
                            log.info("发送：：：{\"pong\":\"{}\"}",time);
                        }
//                        msgByte.accept(buffer);
                    }

                    @Override
                    public void onWebsocketPong(WebSocket conn, Framedata f) {
                        log.info(
                                "[{}]ReConnectWSClient [onWebsocketPong]接收到服务端数据：opcode={}",
                                key,
                                f.getOpcode());
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

