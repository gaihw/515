package com.zmj.demo.config.dataSource;

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
@MapperScan(basePackages = "com.zmj.demo.dao.allin", sqlSessionFactoryRef = "allin102SqlSessionFactory")
public class MybatisAllin102Config {
    @Autowired
    @Qualifier("allin102DataSource")
    private DataSource allin102DataSource;

    @Bean
    public SqlSessionFactory allin102SqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(allin102DataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate allin102SqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(allin102SqlSessionFactory());
        return template;
    }
}
