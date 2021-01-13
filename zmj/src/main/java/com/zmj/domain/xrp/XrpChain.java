package com.zmj.domain.xrp;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class XrpChain {

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
     * 私钥
     */
    private String secret;

    /**
     * 标签
     */
    private String memo;

}
