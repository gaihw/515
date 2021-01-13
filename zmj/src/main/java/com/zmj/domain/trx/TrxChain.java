package com.zmj.domain.trx;

import lombok.Data;

@Data
public class TrxChain {
    /**
     * 转出地址
     */
    private String ownerAddress;

    /**
     * 转入地址
     */
    private String toAddress;

    /**
     * 转出数量
     */
    private String  amount;

    /**
     * 转出地址的私钥
     */
    private String privateKey;

    /**
     * 冻结金额
     */
    private String frozenBalance;

    /**
     * resource 冻结 TRX 获取资源的类型, 可以是 BANDWIDTH 或者 ENERGY
     */
    private String resource;

    /**
     * 接收地址
     */
    private String receiverAddress;

    /**
     * 账户地址
     */
    private String accountAddress;
}
