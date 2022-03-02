package com.zmj.demo.domain.dev1;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClearingTransferChain {
    private BigDecimal amount;
    private BigDecimal profit;
    private BigDecimal fee;
}
