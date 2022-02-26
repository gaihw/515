package com.zmj.demo.domain.dev1;

import lombok.Data;

@Data
public class UserBalanceChain {
    private String userId;
    private String currencyId;
    private String balance;
    private String hold;
}
