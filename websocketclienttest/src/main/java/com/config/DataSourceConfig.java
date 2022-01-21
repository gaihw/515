package com.config;

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
     * dev1数据源:
     *
     * @return
     */
    @Bean(name = "dev1DataSource")
    @Qualifier("dev1DataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.dev1.datasource")
    public DataSource base() {

        return DataSourceBuilder.create().build();
    }

}


