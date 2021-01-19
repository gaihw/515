package com.port.websocket.util;

public enum SideEnum {

    BUY(1), SELL(2);

    int code;

    SideEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
