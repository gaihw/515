package com.port.websocket.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class RestResult implements Serializable {

    private int code;
    private Object data;
    private String message;

}