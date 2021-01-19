package com.test.config.ds.account;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = AccountDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "accountSqlSessionFactory")
public class AccountDataSourceConfig {

    // 精确到 account 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.test.dao.account";
    static final String MAPPER_LOCATION = "classpath:mapper/account/*.xml";

    @Value("${account.datasource.url}")
    private String url;

    @Value("${account.datasource.username}")
    private String user;

    @Value("${account.datasource.password}")
    private String password;

    @Value("${account.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "accountDataSource")
    public DataSource accountDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "accountTransactionManager")
    public DataSourceTransactionManager accountTransactionManager() {
        return new DataSourceTransactionManager(accountDataSource());
    }

    @Bean(name = "accountSqlSessionFactory")
    public SqlSessionFactory accountSqlSessionFactory(@Qualifier("accountDataSource") DataSource accountDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(accountDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(AccountDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}