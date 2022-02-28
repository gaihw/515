package com.zmj.demo.domain.dev1;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBalanceChain {
    private String userId;
    private String currencyId;
    private BigDecimal balance;
    private BigDecimal hold;
}
