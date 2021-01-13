package com.zmj.service;

import com.alibaba.fastjson.JSONObject;
import com.zmj.domain.trx.Trc20Chain;

public interface Trc20Service {
    /**
     * 创建交易
     * @param trc20Chain
     * @return
     */
    String createtransaction(Trc20Chain trc20Chain);

    /**
     * 余额查询
     * @param address
     * @return
     */
    String balanceof(String address);

    /**
     * 查询交易信息
     */
    JSONObject getrawtransaction(String txid);
}
