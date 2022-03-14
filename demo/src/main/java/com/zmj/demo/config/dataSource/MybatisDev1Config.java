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
@MapperScan(basePackages = "com.zmj.demo.dao.dev1", sqlSessionFactoryRef = "dev1SqlSessionFactory")
public class MybatisDev1Config {

    @Autowired
    @Qualifier("dev1DataSource")
    private DataSource dev1DataSource;

    @Bean
    public SqlSessionFactory dev1SqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dev1DataSource);
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate dev1SqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(dev1SqlSessionFactory());
        return template;
    }
}
