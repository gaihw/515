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
    @Bean(name = "testDataSource")
    @Qualifier("testDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.test.datasource")
    public DataSource test() {

        return DataSourceBuilder.create().build();
    }

    /**
     *localhost
     * @return
     */
    @Bean(name = "dev1DataSource")
    @Qualifier("dev1DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.dev1.datasource")
    public DataSource dev1() {

        return DataSourceBuilder.create().build();
    }
}
