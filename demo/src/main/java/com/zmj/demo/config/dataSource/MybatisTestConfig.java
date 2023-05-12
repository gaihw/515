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
@MapperScan(basePackages = "com.zmj.demo.dao.test", sqlSessionFactoryRef = "testSqlSessionFactory")
public class MybatisTestConfig {

    @Autowired
    @Qualifier("testDataSource")
    private DataSource testDataSource;

    @Bean(name = "testSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }
}
