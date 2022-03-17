package com.zmj.demo.domain.dev1;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Market1minBtcusdt {
    /**
     * 时间刻度起始值
     */
    private BigDecimal idx;

    /**
     * 交易额
     */
    private BigDecimal amount;


    /**
     * 交易量
     */
    private BigDecimal vol;


    /**
     * 开盘价
     */
    private BigDecimal open;

    /**
     * close
     */
    private BigDecimal close;

    /**
     * 最高价
     */
    private BigDecimal high;


    /**
     * 最低价
     */
    private BigDecimal low;
}
