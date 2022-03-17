package com.zmj.demo.domain.dev1;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class PositionChain {
    private String id;
    private String positionId;
    private String orderId;
    private String userId;
    private String symbol;
    private int leverage;
    /**
     * CROSSED:全仓模式;FIXED:逐仓模式
     */
    private String marginType;
    private String direction;
    private String side;
    private BigDecimal quantity;
    private BigDecimal closedQuantity;
    private BigDecimal margin;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    /**
     * 开仓均价
     */
    private BigDecimal avgCost;
    private Date openTime;
    private String partnerId;
    private String parentId;
}
