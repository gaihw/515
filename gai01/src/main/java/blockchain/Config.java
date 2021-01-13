package blockchain;

public class Config {

//    btc系列
    public static String BTC_USERNMAME = "btctest";
    public static String BTC_PASSWORD = "btctest";

    public static String BTC_URL = "192.168.112.66";
    public static String BTC_PORT = "9338";

    public static String BSV_URL = "192.168.112.214";
    public static String BSV_PORT = "9332";

    public static String DASH_URL = "192.168.112.66";
    public static String DASH_PORT = "6332";

//    test
    public static String BCHA_URL = "192.168.112.66";
    public static String BCHA_PORT = "9332";

    //test
    public static String BCH_URL = "192.168.112.135";
    public static String BCH_PORT = "9334";
    //online
//    public static String BCH_URL = "192.168.112.89";
//    public static String BCH_PORT = "8382";

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
    public static String HT = "0x0da0f00d752c432267eb08b4891d5492983e8137";
    public static String OKB = "0x4390b3a9612b16e41545d811ba7b92b5e2951858";
    public static String LINK = "0x514910771af9ca656af840dff83e8264ecf986ca";
    //test
    public static String ETC_URL = "192.168.112.66";
    public static String ETC_PORT = "8646";
    //online
//    public static String ETC_URL = "13.231.78.92";
//    public static String ETC_PORT = "8545";

//    eos
    public static String EOS_URL = "192.168.112.214";
    public static String EOS_NODEOSD_PORT = "8888";
    public static String EOS_KEOSD_PORT = "8889";
    //fex冷钱包账户
    public static String EOS_COLD_69_ACCOUNT = "eosofdeposit";
    public static String EOS_HOT_69 = "withdraweos1";
    //58冷钱包账户
    public static String EOS_COLD_58_ACCOUNT = "cold";
    public static String EOS_HOT_58 = "hot";
    public static String WALLET = "dev";
    public static String WALLET_PASSWORD = "PW5JozcW8g3pxbYLJuCyQvx1y31SDk4b8q58R3mGz2LLUvcSPvqGQ";

    //    xrp
    public static String XRP_URL = "https://s.altnet.rippletest.net:51234";
    //fex冷钱包账户
    public static String XRP_COLD_69_ACCOUNT = "rPX66MjtvbpgE5sQjM7BwNci7UpPkbmAjH";
    //58冷钱包账户
    public static String XRP_COLD_58_ACCOUNT = "raDPvocc8DQbDNfqG4DLRyWmL98PhLPY4f";


    //trx
    //test 211
//    public static String TRX_URL = "192.168.112.211";
//    public static String TRX_PORT = "16667";
//    public static String USDT_TRC20 = "41cc97c991df6fc3b6f5474b344cab6203ebd14398";
    //test 214
    public static String TRX_URL = "192.168.112.214";
    public static String TRX_PORT = "16667";
    public static String USDT_TRC20 = "4182e4d6f39fd15feeefebcd1c0247b7e78059654a";
//    online
//    public static String TRX_URL = "13.231.78.92";
//    public static String TRX_PORT = "58090";
//    public static String USDT_TRC20 = "41a614f803b6fd780986a42c78ec9c7f77e6ded13c";
//    public static String TRX_URL = "47.242.80.9";
//    public static String TRX_PORT = "58090";
//    public static String USDT_TRC20 = "41a614f803b6fd780986a42c78ec9c7f77e6ded13c";


    public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL_BIGDATA = "jdbc:mysql://192.168.112.36:3306/bigdata";
    public static final String DB_URL_58RISK = "jdbc:mysql://192.168.112.36:3306/58risk";
    public static final String DB_URL_58ACCOUNT = "jdbc:mysql://192.168.112.36:3306/58account";
    public static final String DB_URL_58swap = "jdbc:mysql://192.168.112.31:3306/58swap";
    public static final String DB_URL_58future = "jdbc:mysql://192.168.200.86:3306/58future";
    public static final String DB_URL_58regular = "jdbc:mysql://192.168.200.86:3306/58regular_future";
    public static final String DB_URL_58mix = "jdbc:mysql://192.168.200.86:3306/mix_contract";
    public static final String DB_URL_58STATISTICS = "jdbc:mysql://192.168.112.36:3306/58statistics";
    public static final String DB_URL_58RISK_WITHDRAWAL = "jdbc:mysql://192.168.112.36:3306/58risk_withdrawal";
    public static final String DB_URL_FEXWALLET = "jdbc:mysql://192.168.112.36:3306/fex_wallet";
    public static final String DB_URL_58WALLET = "jdbc:mysql://192.168.112.36:3306/58wallet";

    // MySQL 8.0 以上版本 - JDBC 驱动名及数据库 URL
    //static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    //static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB?useSSL=false&serverTimezone=UTC";


    // 数据库的用户名与密码，需要根据自己的设置
    public static final String USER_36 = "root";
    public static final String PASS_36 = "58test";
    public static final String USER_31 = "root";
    public static final String PASS_31 = "58mysql456";
    public static final String USER_86 = "root";
    public static final String PASS_86 = "123456";
    public static final String AUTHORIZATION = "Bearer 881728b6-f77d-48bc-4f62-8252-20e61ae81114";





}
