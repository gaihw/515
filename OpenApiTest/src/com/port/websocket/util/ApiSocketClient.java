package com.port.websocket.util;

import okhttp3.WebSocketListener;

import java.io.Closeable;

/**
 * 58coin API data streaming façade, supporting streaming of events through web sockets.
 */
public interface ApiSocketClient {

    /**
     * Open a new web socket to receive {@link DepthEvent depthEvents} on a callback.
     *
     * @param products market product(s) number (one or coma-separated) to subscribe to
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
//    Closeable onDepthEvent(String products, ApiCallback<DepthEvent> callback);

    /**
     * Open a new web socket to receive {@link KlineEvent klineEvents} on a callback.
     *
     * @param products market product(s) number (one or coma-separated) to subscribe to
     * @param interval the interval of the candles tick events required
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
    Closeable onKlineEvent(String products, KlineIntervalEnum interval, ApiCallback<KlineEvent> callback);

    /**
     * Open a new web socket to receive {@link TickerEvent tickerEvents} on a callback.
     *
     * @param products market product(s) number (one or coma-separated) to subscribe to
     * @param callback the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
//    Closeable onTickerEvent(String products, ApiCallback<TickerEvent> callback);

    /**
     * Open a new web socket to receive {@link OrderUpdateEvent orderUpdateEvents} on a callback.
     *
     * @param businesses the business name of subscription.
     * @param callback   the callback to call on new events
     * @return a {@link Closeable} that allows the underlying web socket to be closed.
     */
//    Closeable onOrderUpdateEvent(String businesses, ApiCallback<OrderUpdateEvent> callback);


//    Closeable createWebSocket(String channel, WebSocketListener webSocketListener);

}
