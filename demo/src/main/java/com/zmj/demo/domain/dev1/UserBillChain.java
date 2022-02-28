package com.zmj.demo.domain.dev1;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBillChain {
    private String userId;
    private int type;
    private BigDecimal size;
    private BigDecimal preBalance;
    private BigDecimal postBalance;
    private BigDecimal preProfit;
    private BigDecimal postProfit;
    private BigDecimal preMargin;
    private BigDecimal postMargin;
    private String partnerId;
    private String parentId;
    private String sourceId;
    private String note;
    private String createdDate;
    private String fromUserId;
    private BigDecimal total;

}
