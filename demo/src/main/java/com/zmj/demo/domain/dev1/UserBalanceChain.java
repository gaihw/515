package com.zmj.demo.domain.dev1;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBalanceChain {
    private String userId;
    private String currencyId;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 开仓冻结
     */
    private BigDecimal hold;
    /**
     * 转出冻结
     */
    private BigDecimal freeze;
    /**
     * 平台借款资金
     */
    private BigDecimal borrow;
}
