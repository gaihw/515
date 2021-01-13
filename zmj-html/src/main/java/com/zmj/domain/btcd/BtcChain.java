package com.zmj.domain.btcd;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BtcChain {

    /**
     * 交易id
     */
    private String txid;

    /**
     * vout 序列
     */
    private int vout;

    /**
     * 转出地址
     */
    private String fromAddress;

    /**
     * 转入地址
     */
    private String toAddress;

    /**
     * 转账金额
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 公钥脚本
     */
    private String scriptPubKey;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 赎回脚本
     */
    private String redeemScript;

}
