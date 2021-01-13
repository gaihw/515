package com.zmj.service;

import com.zmj.domain.btcd.BtcChain;

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
    String sendtoaddress(BtcChain btcChain);

    /**
     * 转账 方式二
     */
//    String createrawtransaction(String txid,String vout,String toAddress,String value);

    /**
     * 查询交易信息
     */
    String getrawtransaction(String txId);

    /**
     * 生成地址
     * @return
     */
    String getnewaddress();
}
