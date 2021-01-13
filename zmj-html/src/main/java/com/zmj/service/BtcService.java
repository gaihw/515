package com.zmj.service;

import java.math.BigDecimal;

public interface BtcService {

    /**
     * 查询区块高度
     */
    String getblockcount();

    /**
     * getpeerinfo
     */
    String getpeerinfo();

    /**
     * 转账 方式一 sendtoaddress
     */
    String sendtoaddress(String toAddress,String value);

    /**
     * 转账 方式二
     */
    String createrawtransaction(String txid,String vout,String toAddress,String value);
}
