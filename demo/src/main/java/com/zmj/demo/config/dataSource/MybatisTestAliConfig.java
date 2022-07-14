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
@MapperScan(basePackages = "com.zmj.demo.dao.testAli", sqlSessionFactoryRef = "testAliSqlSessionFactory")
public class MybatisTestAliConfig {
    @Autowired
    @Qualifier("testAliDataSource")
    private DataSource testAliDataSource;

    @Bean
    public SqlSessionFactory testAliSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(testAliDataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate testAliSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(testAliSqlSessionFactory());
        return template;
    }
}
