package com.zmj.service;

import com.zmj.domain.xrp.XrpChain;

import java.math.BigDecimal;


public interface XrpService {
    /**
     * 交易
     * @param xrpChain
     * @return
     */
    String transaction(XrpChain xrpChain);

    /**
     * 查询账户信息
     * @param account
     * @return
     */
    String getAccountInfo(String account);

    /**
     * 查询交易信息
     * @param transaction
     * @return
     */
    String getTransaction(String transaction);
}
