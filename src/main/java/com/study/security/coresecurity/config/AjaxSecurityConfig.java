package com.study.security.coresecurity.config;

import com.study.security.coresecurity.provider.AjaxAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Order(0)
@Slf4j
public class AjaxSecurityConfig {

    @Bean
    public AuthenticationManager configure(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .authorizeRequests()
                .anyRequest().authenticated();
        http
                .csrf().disable();
        /**
         * 필터처리
         * addFilterBefore(): 실제 추가하고자 하는 필터가 기존 필터 앞에 위치할 때
         * addFilter(): 가장 마지막에 위치할 때
         * addFilterAfter(): 실제 추가하고자 하는 필터가 기존 필터 뒤에 위치할 때
         * addFilterAt(): 현재 기존의 필터 위치를 대체하고자 할 때
         */
//        http
//                .addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class); // UsernamePasswordAuthenticationFilter 필터 앞에 ajax 필터 위치하도록 한다.


        return http.build();
    }

    //    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return authentication -> {};
//    }
//
//    @Bean
//    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() {
//        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
//        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBean());
//        return ajaxLoginProcessingFilter; // 인증 처리용 필터 생성
//    }
}
