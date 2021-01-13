package com.zmj.config.mybatisConfig;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    /**
     * 主数据源:192.168.112.36
     *
     * @return
     */
    @Bean(name = "baseDataSource")
    @Qualifier("baseDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.base.datasource")
    public DataSource base() {

        return DataSourceBuilder.create().build();
    }

    /**
     * future数据源:192.168.200.86
     *
     * @return
     */
    @Bean(name = "futureDataSource")
    @Qualifier("futureDataSource")
    @ConfigurationProperties(prefix = "spring.future.datasource")
    public DataSource future() {

        return DataSourceBuilder.create().build();
    }

    /**
     * regular数据源:192.168.200.86
     *
     * @return
     */
    @Bean(name = "regularDataSource")
    @Qualifier("regularDataSource")
    @ConfigurationProperties(prefix = "spring.regular.datasource")
    public DataSource regular() {

        return DataSourceBuilder.create().build();
    }

    /**
     * platform数据源:192.168.112.17
     *
     * @return
     */
    @Bean(name = "platformDataSource")
    @Qualifier("platformDataSource")
    @ConfigurationProperties(prefix = "spring.platform.datasource")
    public DataSource platform() {

        return DataSourceBuilder.create().build();
    }
}


