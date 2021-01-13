package com.test.db;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Slf4j
public class JdbcTemplateMgr {


    private volatile static JdbcTemplateMgr instance = null;

    private JdbcTemplate jdbcTemplate;

    private DruidDataSource dataSource;

    private JdbcTemplateMgr() {
    }

    //只调用一次
    public void initJdbcTemplate(boolean isTest) {
        initDataSource(isTest);
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    private void initDataSource(boolean isTest) {
        if (dataSource != null) {
            return;
        }
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");

        if (isTest) {
            dataSource.setUrl("jdbc:mysql://192.168.112.36:3306/58wallet");
            dataSource.setPassword("58test");
        } else {
            dataSource.setUrl("jdbc:mysql://192.168.112.90:3306/blockchain");
            dataSource.setPassword("wallet");
        }

        dataSource.setInitialSize(5);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(10);
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    public static JdbcTemplateMgr getInstance() {
        if (instance == null) {
            synchronized (JdbcTemplateMgr.class) {
                if (instance == null) {
                    instance = new JdbcTemplateMgr();
                }
            }
        }
        return instance;
    }

    public static JdbcTemplate getJdbcTemplate() {
        return JdbcTemplateMgr.getInstance().jdbcTemplate;
    }

    public static DruidDataSource getDataSource() {
        return JdbcTemplateMgr.getInstance().dataSource;
    }

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

    //list 形式 只有值 没有key 查询多列
    public static <T> List query(String sql, String[] cols) {
        return query(getJdbcTemplate(), sql, cols);

    }

    public static <T> List<T> query(String sql, String col) {
        return (List) getJdbcTemplate().query(sql, (rs, rowNum) -> rs.getString(col));
    }


}
