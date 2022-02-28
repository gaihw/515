package com.zmj.demo.config;

import java.math.BigDecimal;

public class Config {

//    btc系列
    public static String BTC_USERNMAME = "btctest";
    public static String BTC_PASSWORD = "btctest";

    public static String BTC_URL = "192.168.112.68";
    public static String BTC_PORT = "9338";

    public static String BSV_URL = "192.168.112.214";
    public static String BSV_PORT = "9332";

    public static String DASH_URL = "192.168.112.66";
    public static String DASH_PORT = "6332";

    public static String BCH_URL = "192.168.112.68";
    public static String BCH_PORT = "9332";

    public static String OMNI_URL = "192.168.112.68";
    public static String OMNI_PORT = "9338";
    public static String PROPERTYID = "2147483651";
    public static String OMNI_ADDRESS = "2NFbGzEq2HBchX6s6Ac4Xpk7znEnEYJ9Rcq";//部署合约时，该地址初始化，usdt钱最多

    public static String LTC_URL = "192.168.112.68";
    public static String LTC_PORT = "19443";

    public static String ZEC_URL = "192.168.112.211";
    public static String ZEC_PORT = "18232";

//    eth系列
    public static String ETH_URL = "192.168.112.214";
    public static String ETH_PORT = "8545";
    public static byte ETH_CHAINLD = 101;
    public static byte ETC_CHAINLD = 102;
    public static String USDT_ERC20 = "0xbef56c9933678280fb18848d3fe85031f4714d09";
    public static String HT = "0x8a154a5de374dddfc8f1ce362966445c6f258760";
    public static String OKB = "0x75231f58b43240c9718dd58b4967c5114342a86c";
    public static String LINK = "0x514910771af9ca656af840dff83e8264ecf986ca";
    public static String ETC_URL = "192.168.112.66";
    public static String ETC_PORT = "8646";

//    eos
    public static String EOS_URL = "192.168.112.214";
    public static String EOS_NODEOSD_PORT = "8888";
    public static String EOS_KEOSD_PORT = "8889";
    public static String EOS_COLD_69_ACCOUNT = "eosofdeposit";
    public static String EOS_COLD_50_ACCOUNT = "cold";
    public static String WALLET = "dev";
    public static String WALLET_PASSWORD = "PW5JozcW8g3pxbYLJuCyQvx1y31SDk4b8q58R3mGz2LLUvcSPvqGQ";
    public static String ACCOUNT = "eosio";

    //    xrp
    public static String XRP_URL = "https://s.altnet.rippletest.net:51234";
    public static String XRP_COLD_69_ACCOUNT = "rPX66MjtvbpgE5sQjM7BwNci7UpPkbmAjH";
    public static String XRP_COLD_50_ACCOUNT = "raDPvocc8DQbDNfqG4DLRyWmL98PhLPY4f";
    public static String XRP_FROM_ADDRESS = "rfRBucPC5mPASNb9Nn2MxaXxPggTdUCNjn";
    public static String XRP_FROM_ADDRESS_SECRET = "shB2tUtz2tBYvaYJoR8GXPX6bdELe";
    public static BigDecimal XRP_FEE = BigDecimal.valueOf(0.00001);

    //trx
    //13.231.78.92:58090
    public static String TRX_URL = "192.168.112.214";
    public static String TRX_PORT = "16667";
    public static String TRC20_CONTRANCT_ADDRESS = "4181aebbded41c5372b6e44a2f0d47ff60fe488f09";
    public static String TRC20_OWNER_ADDRESS = "414311f08873669b35f995e0bfb603509e1d96c380";
    public static String TRC20_PRIVATEKEY = "61cae49321a8f13788392b9b4b76dd3410007bb8aca89acdbefe0692d175c684";
    public static String TRX_OWNER_ADDRESS = "41eaa0cda6897646bf6a98953c2d5ee78f9ad6e113";
    public static String TRX_PRIVATEKEY = "f8611b717e20ca143678fff2d363136be448d9e23019e9bc345551f4e8fde515";
    public static String CALLVALUE = "0";

    //redis设置过期时间
    public static Long MAX_TIME = Long.valueOf(5*24*60*60);

    //手续费率
    public static final BigDecimal taker = new BigDecimal(0.0004);

    //顶级合伙人
    public static final String high_partner = "51904446";






}
