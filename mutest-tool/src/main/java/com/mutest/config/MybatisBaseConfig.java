package com.mutest.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author guozhengMu
 * @version 1.0
 * @date 2019/1/2 18:42
 * @description 主库数据源配置
 * @modify
 */
@Configuration
@MapperScan(basePackages = "com.mutest.dao.base", sqlSessionFactoryRef = "baseSqlSessionFactory")
public class MybatisBaseConfig {
    @Autowired
    @Qualifier("baseDataSource")
    private DataSource baseDataSource;

    @Bean
    public SqlSessionFactory baseSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(baseDataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate baseSqlSessionTemplate() throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(baseSqlSessionFactory());
        return template;
    }
}
