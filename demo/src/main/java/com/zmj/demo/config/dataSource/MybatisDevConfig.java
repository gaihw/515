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
@MapperScan(basePackages = "com.zmj.demo.dao.dev", sqlSessionFactoryRef = "devSqlSessionFactory")
public class MybatisDevConfig {
    @Autowired
    @Qualifier("devDataSource")
    private DataSource devDataSource;

    @Bean(name = "devSqlSessionFactory")
    public SqlSessionFactory devSqlSessionFactory(@Qualifier("devDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate devSqlSessionTemplate(@Qualifier("devSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }
}
