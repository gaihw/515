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
 * @description  regular项目数据源配置
 * @modify
 */
@Configuration
@MapperScan(basePackages = "com.mutest.dao.regular", sqlSessionFactoryRef = "regularSqlSessionFactory")
public class MybatisRegularConfig {
    @Autowired
    @Qualifier("regularDataSource")
    private DataSource regularDataSource;

    @Bean
    public SqlSessionFactory regularSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(regularDataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate regularSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(regularSqlSessionFactory());
        return template;
    }
}