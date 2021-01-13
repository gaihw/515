package com.zmj.domain.eos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EosChain {

    /**
     * 钱包
     */
    private String wallet;

    /**
     * 密码
     */
    private String password;

    /**
     * 私钥
     */
    private String privateKey;

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
    private BigDecimal value;

    /**
     * 标签
     */
    private String memo;

    /**
     * 账户
     */
    private String account;

}
