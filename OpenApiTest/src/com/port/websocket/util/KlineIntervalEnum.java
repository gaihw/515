package com.port.websocket.util;

/**
 * Kline intervals
 */
public enum KlineIntervalEnum {

    ONE_MINUTE("1min"),
    THREE_MINUTES("3min"),
    FIVE_MINUTES("5min"),
    FIFTEEN_MINUTES("15min"),
    HALF_HOURLY("30min"),
    HOURLY("1hour"),
    TWO_HOURLY("2hour"),
    FOUR_HOURLY("4hour"),
    SIX_HOURLY("6hour"),
    TWELVE_HOURLY("12hour"),
    DAILY("1day"),
    WEEKLY("1week");

    private final String intervalId;

    KlineIntervalEnum(String intervalId) {
        this.intervalId = intervalId;
    }

    public String getIntervalId() {
        return intervalId;
    }
}
