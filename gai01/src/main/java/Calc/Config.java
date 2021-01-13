package Calc;

public class Config {
    public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DB_URL_BIGDATA = "jdbc:mysql://192.168.112.36:3306/bigdata";
    public static final String DB_URL_58RISK = "jdbc:mysql://192.168.112.36:3306/58risk";
    public static final String DB_URL_58swap = "jdbc:mysql://192.168.112.31:3306/58swap";
    public static final String DB_URL_58future = "jdbc:mysql://192.168.200.86:3306/58future";
    public static final String DB_URL_58regular = "jdbc:mysql://192.168.200.86:3306/58regular_future";
    public static final String DB_URL_58mix = "jdbc:mysql://192.168.200.86:3306/mix_contract";
    public static final String DB_URL_58STATISTICS = "jdbc:mysql://192.168.112.36:3306/58statistics";
    public static final String DB_URL_58RISK_WITHDRAWAL = "jdbc:mysql://192.168.112.36:3306/58risk_withdrawal";
    public static final String DB_URL_FEXWALLET = "jdbc:mysql://192.168.112.36:3306/fex_wallet";
    public static final String DB_URL_58WALLET = "jdbc:mysql://192.168.112.36:3306/58wallet";
    public static final String DB_URL_36 = "jdbc:mysql://192.168.112.36:3306";
    public static final String DB_URL_USDT = "";

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
