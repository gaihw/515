package com.zmj.domain.btcd;


import lombok.Data;

@Data
public class BtcRestChain {
    private String result;
    private String error;
    private String id;
    private int code;
}
