package com.zlm.util.websocketclient.webSocketClient.inter;

/**
 * @author: create by zlm
 * @version: v1.0
 * @description: com.zlm.util.websocket.webSocketClient.inter
 * @date:2019-10-19
 **/
public interface WebSocketService {
    /**
     * 群发
     * @param message
     */
    void groupSending(String message);

    /**
     * 指定发送
     * @param name
     * @param message
     */
    void appointSending(String name, String message);
}
