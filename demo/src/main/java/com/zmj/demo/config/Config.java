package com.zmj.demo.config;

import java.math.BigDecimal;

public class Config {

    //redis设置过期时间
    public static Long MAX_TIME = Long.valueOf(5*24*60*60);

    //手续费率
    public static final BigDecimal taker = new BigDecimal(0.0004);

    //顶级合伙人
    public static final String high_partner = "51894801";

    //流水默认开始计算时间
    public static final String startTime = "2022-02-25 00:00:00";

    //精度
    public static int newScale = 6;

    //合伙人type类型不是0，则配置默认比例
    public static BigDecimal partnerTransferOutRatio = BigDecimal.valueOf(0.7);

    //风险率
    public static BigDecimal rate = BigDecimal.valueOf(0.1);

    //手续费返佣的层级
    public static final int level = 2;





}
