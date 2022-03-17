package com.zmj.demo.config;

import java.math.BigDecimal;

public class Config {

    //redis设置过期时间
    public static Long MAX_TIME = Long.valueOf(5*24*60*60);

    //手续费率
    public static final BigDecimal taker = new BigDecimal(0.0004);

    //顶级合伙人
    public static final String high_partner = "51904446";

    public static final String startTime = "2022-02-25 00:00:00";

    public static int newScale = 6;

    public static BigDecimal partnerTransferOutRatio = BigDecimal.valueOf(0.7);

    public static BigDecimal rate = BigDecimal.valueOf(0.1);





}
