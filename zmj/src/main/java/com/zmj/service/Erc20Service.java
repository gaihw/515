package com.zmj.service;

import com.zmj.domain.eth.Erc20Chain;

public interface Erc20Service {
    /**
     * 创建交易
     */
    String transaction(Erc20Chain erc20Chain);

    /**
     * 查询交易信息
     */
    String getrawtransaction(String txid);
}
