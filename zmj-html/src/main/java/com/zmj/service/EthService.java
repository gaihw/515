package com.zmj.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.eth.EthChain;

import java.math.BigInteger;

public interface EthService {
    /**
     * 获取地址，公私钥
     * @return
     */
    JSONObject getAddPrivPub(EthChain ethChain);

    /**
     * 查询区块高度
     */
    BigInteger getBlockNumber(EthChain ethChain);

    /**
     * 创建交易
     * @param ethChain
     * @return
     */
    String transaction(EthChain ethChain);

}
