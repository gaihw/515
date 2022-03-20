package com.zmj.demo.domain.dev1;

import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

@Data
public class UserInfoDataChain {
    private String userId;
    private String mobile;
    private BigDecimal balance;
    private BigDecimal hold;

    private BigDecimal otcBalance;
    private BigDecimal assetBalance;
}
