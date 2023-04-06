package com.study.security.config.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() // 요청에 대한 보안 검사를 하겠다
                .antMatchers("/user").hasRole("USER") // /user 경로의 경우 USER 권한을 가진 사용자만 인가 허용
                .anyRequest().permitAll(); // 나머지 접근에 대해서는 모두 허용

        http.formLogin();
        return http.build();
    }
}
