package com.port.websocket.util;

/**
 * An exception which can occur while invoking methods of the 58coin API.
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 3788669840036201041L;

    public ApiException() {
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
