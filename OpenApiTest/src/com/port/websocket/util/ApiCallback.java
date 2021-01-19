package com.port.websocket.util;


/**
 * ApiCallback is a functional interface used together with the ApiAsyncClient to provide a non-blocking REST client.
 *
 * @param <T> the return type from the callback
 */
@FunctionalInterface
public interface ApiCallback<T> {

    /**
     * Called whenever a response comes back from the 58coin API.
     *
     * @param response the expected response object
     */
    void onResponse(T response);

    /**
     * Called whenever an error occurs.
     *
     * @param cause the cause of the failure
     */
    default void onFailure(Throwable cause) {
    }

    /**
     * Called when the connection is closed
     */
    default void onClosed(int code, String reason) {
    }

    default void onConnected(SubMessage message) {
    }
}