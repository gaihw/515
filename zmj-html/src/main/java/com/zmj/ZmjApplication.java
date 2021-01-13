package com.zmj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zmj.dao")// mapper 接口类扫描包配置
@EnableAutoConfiguration
public class ZmjApplication {

	public static void main(String[] args) {

		SpringApplication.run(ZmjApplication.class, args);
	}

}
