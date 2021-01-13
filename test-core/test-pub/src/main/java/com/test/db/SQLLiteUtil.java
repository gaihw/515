package com.test.db;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
public class SQLLiteUtil {


    private volatile static SQLLiteUtil instance = null;

    private JdbcTemplate cfgTemplate;

    //BTCD
    private JdbcTemplate btcTemplate;
    private JdbcTemplate bchTemplate;
    private JdbcTemplate bsvTemplate;
    private JdbcTemplate ltcTemplate;
    private JdbcTemplate dashTemplate;
    private JdbcTemplate zecTemplate;
    private JdbcTemplate usdtTemplate;

    private JdbcTemplate ethTemplate;


    //只调用一次
    public void initJdbcTemplate() {
        //this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    private DruidDataSource createDruidDataSource(String dbName) {
        DruidDataSource cfgDS = new DruidDataSource();
        cfgDS.setDriverClassName("org.sqlite.JDBC");
        cfgDS.setUrl("jdbc:sqlite:" + System.getProperty("user.home") + "/fex/" + dbName + ".db");
        return cfgDS;
    }

    public void initCfgJdbcTemplate() {
        this.cfgTemplate = new JdbcTemplate(createDruidDataSource("config"));
    }


    public void initJdbcTemplate(List<String> coinArr) {


        if (coinArr == null) {
            return;
        }
        //config
        for (String dbName : coinArr) {
            JdbcTemplate dbTemplate = new JdbcTemplate(createDruidDataSource((String) dbName));
            switch (dbName) {
                case "BTC":
                    this.btcTemplate = dbTemplate;
                    break;
                case "BCH":
                    this.bchTemplate = dbTemplate;
                    break;
                case "BSV":
                    this.bsvTemplate = dbTemplate;
                    break;
                case "LTC":
                    this.ltcTemplate = dbTemplate;
                    break;
                case "DASH":
                    this.dashTemplate = dbTemplate;
                    break;
                case "ZEC":
                    this.zecTemplate = dbTemplate;
                    break;
                case "USDT":
                    this.usdtTemplate = dbTemplate;
                    break;
                case "OMNI-USDT":
                    this.usdtTemplate = dbTemplate;
                    break;
                case "ETH":
                    this.ethTemplate = dbTemplate;
                    break;
            }
        }
    }

    public static SQLLiteUtil getInstance() {
        if (instance == null) {
            synchronized (SQLLiteUtil.class) {
                if (instance == null) {
                    instance = new SQLLiteUtil();
                }
            }
        }
        return instance;
    }

    public static JdbcTemplate getCfgJdbcTemplate() {
        return SQLLiteUtil.getInstance().cfgTemplate;
    }

    public static JdbcTemplate getEthJdbcTemplate() {
        return SQLLiteUtil.getInstance().ethTemplate;
    }

    public static JdbcTemplate getBtcJdbcTemplate(String coinName) {
        switch (coinName) {
            case "BTC":
                return SQLLiteUtil.getInstance().btcTemplate;
            case "BCH":
                return SQLLiteUtil.getInstance().bchTemplate;
            case "BSV":
                return SQLLiteUtil.getInstance().bsvTemplate;
            case "LTC":
                return SQLLiteUtil.getInstance().ltcTemplate;
            case "DASH":
                return SQLLiteUtil.getInstance().dashTemplate;
            case "ZEC":
                return SQLLiteUtil.getInstance().zecTemplate;
            case "OMNI-USDT":
                return SQLLiteUtil.getInstance().usdtTemplate;
            case "USDT":
                return SQLLiteUtil.getInstance().usdtTemplate;
            case "OMNI":
                return SQLLiteUtil.getInstance().usdtTemplate;
        }
        return null;
    }

    public static JdbcTemplate getUsdtJdbcTemplate() {
        return SQLLiteUtil.getInstance().usdtTemplate;
    }


    public static <T> List query(String coinName, String sql, String[] cols) {
        return query(SQLLiteUtil.getBtcJdbcTemplate(coinName), sql, cols);
    }

    //list 形式 只有值 没有key 查询多列
    public static <T> List query(JdbcTemplate jdbcTemplate, String sql, String[] cols) {
        log.info("SQL : {}", sql);
        List<Object[]> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Object[] arr = new Object[cols.length];
            int idx = 0;
            for (String col : cols) {
                arr[idx] = rs.getObject(col);
                idx++;
            }
            return arr;
        });
        return list;
    }

    public static <T> List<T> query(JdbcTemplate template, String sql, String col) {
        log.info("SQL : {}", sql);
        return (List) template.query(sql, (rs, rowNum) -> rs.getString(col));
    }


    public boolean insert(String tabName, String[] columns, List list) {
        return true;
    }

    public boolean insert(String tabName, String columns, Map list) {
        return true;
    }

    ;

}
