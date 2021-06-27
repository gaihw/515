package com.zmj.demo.config.dataSource;

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
     *10.90.0.102
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
     *localhost
     * @return
     */
    @Bean(name = "allin102DataSource")
    @Qualifier("allin102DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.allin102.datasource")
    public DataSource allin102() {

        return DataSourceBuilder.create().build();
    }
}
