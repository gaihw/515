package com.zmj.config.mybatisConfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.zmj.dao.currency", sqlSessionFactoryRef = "currencySqlSessionFactory")
public class MybatisCurrencyConfig {
    @Autowired
    @Qualifier("regularDataSource")
    private DataSource regularDataSource;

    @Bean
    public SqlSessionFactory currencySqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(regularDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate curencySqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(currencySqlSessionFactory());
        return template;
    }
}