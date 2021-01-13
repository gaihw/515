package com.zmj.service;

import com.zmj.domain.eos.EosChain;

public interface EosService {

    /**
     * wallet_get_public_keys
     */
    String get_public_keys();

    /**
     * 解锁钱包
     */
    String unlock(EosChain eosChain);

    /**
     * 获取块高
     * @return
     */
    String getblock();

    /**
     * 创建交易
     */
    String transaction(EosChain eosChain);

    /**
     * 打开钱包
     */
    String open(String testcreate);
}
