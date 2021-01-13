package com.zmj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.zmj.dao")// mapper 接口类扫描包配置
//@EnableEurekaClient
//@EnableFeignClients
@EnableScheduling
public class ZmjApplication {

	public static void main(String[] args) {

		SpringApplication.run(ZmjApplication.class, args);
	}

}
