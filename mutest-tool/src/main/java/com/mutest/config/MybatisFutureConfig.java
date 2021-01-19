package com.mutest.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author guozhengMu
 * @version 1.0
 * @date 2019/1/2 18:44
 * @description  Future项目数据源配置
 * @modify
 */
@Configuration
@MapperScan(basePackages = "com.mutest.dao.future", sqlSessionFactoryRef = "futureSqlSessionFactory")
public class MybatisFutureConfig {
    @Autowired
    @Qualifier("futureDataSource")
    private DataSource futureDataSource;

    @Bean
    public SqlSessionFactory futureSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(futureDataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate futureSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(futureSqlSessionFactory());
        return template;
    }
}