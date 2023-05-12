package com.zmj.demo.config.dataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    /**
     * @return
     */
    @Bean(name = "baseDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.base.datasource")
    public DataSource base() {

        return DataSourceBuilder.create().build();
    }


    /**
     *localhost
     * @return
     */
    @Bean(name = "devDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.dev.datasource")
    public DataSource dev() {

        return DataSourceBuilder.create().build();
    }


    /**
     *localhost
     * @return
     */
    @Bean(name = "testDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.test.datasource")
    public DataSource test() {

        return DataSourceBuilder.create().build();
    }

}
