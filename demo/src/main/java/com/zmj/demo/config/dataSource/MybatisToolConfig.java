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
@MapperScan(basePackages = "com.zmj.demo.dao.tool", sqlSessionFactoryRef = "toolSqlSessionFactory")
public class MybatisToolConfig {
    @Autowired
    @Qualifier("toolDataSource")
    private DataSource toolDataSource;

    @Bean
    public SqlSessionFactory toolSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(toolDataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate toolSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(toolSqlSessionFactory());
        return template;
    }
}
