package com.zmj.domain.btcd;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OmniChain {


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
     * 代币序号
     */
    private String propertyid;


}
