package com.study.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@ComponentScan(basePackages = {"com.study.security.config.v1", "com.study.security.controller"})
//@ComponentScan(basePackages = {"com.study.security.config.v2", "com.study.security.controller"})
@ComponentScan(basePackages = {"com.study.security.config.v3", "com.study.security.controller"})
@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

}
