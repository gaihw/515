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
@MapperScan(basePackages = "com.zmj.demo.dao.demodata", sqlSessionFactoryRef = "baseSqlSessionFactory")
public class MybatisDemoDataConfig {
    @Autowired
    @Qualifier("baseDataSource")
    private DataSource baseDataSource;

    @Bean
    public SqlSessionFactory baseSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(baseDataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate baseSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(baseSqlSessionFactory());
        return template;
    }
}
