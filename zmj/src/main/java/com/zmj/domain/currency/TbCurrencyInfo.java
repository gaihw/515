package com.zmj.domain.currency;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TbCurrencyInfo {
    /**
     * 主键id
     */
    private int id;

    /**
     * 币种id
     */
    private String currencyId;

    /**
     * 币种类型
     */
    private String type;

    /**
     * 币种名称
     */
    private String name;

    /**
     * 余额不足报警阈值
     */
    private BigDecimal alarmBalance;

    /**
     * 充提确认数 冷_热_提
     */
    private String receiveNum;

    /**
     * 状态标示
     */
    private Integer statusFlag;

    /**
     * 钱包地址
     */
    private String rpcUrl;

    /**
     * 手续费倍率系数
     */
    private BigDecimal feeFactor;

    /**
     *
     */
    private Integer signType;

    /**
     * 冷钱包余额
     */
    private BigDecimal coldBalance;

    /**
     * 钱包地址
     */
    private String rpcUrlBk;

    /**
     * 是否开启
     */
    private int status;

}
