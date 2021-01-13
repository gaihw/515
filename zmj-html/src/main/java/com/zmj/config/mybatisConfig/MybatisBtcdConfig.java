package com.zmj.config.mybatisConfig;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.zmj.dao.btcd", sqlSessionFactoryRef = "btcdSqlSessionFactory")
public class MybatisBtcdConfig {
    @Autowired
    @Qualifier("regularDataSource")
    private DataSource regularDataSource;

    @Bean
    public SqlSessionFactory btcdSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(regularDataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate btcdSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(btcdSqlSessionFactory());
        return template;
    }
}