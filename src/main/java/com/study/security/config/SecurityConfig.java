package com.study.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()  // 요청에 대한 보안 검사를 하겠다
                .anyRequest().authenticated(); // 현재는 어떤 요청에도 인증 받도록 설정

        http
                .formLogin()
                .loginPage("/loginPage")  // 직접 정의한 로그인 페이지로 이동
                .defaultSuccessUrl("/")  // successHandler() 없는 경우, 이 defaultSuccessUrl이 동작
                .failureUrl("/login")
                .usernameParameter("userId") // default 는 username
                .passwordParameter("passwd") // default 는 password
                .loginProcessingUrl("/login_proc")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("authentication name={}", authentication.getName());
                        response.sendRedirect("/");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        log.info("exception message={}", exception.getMessage());  // exception message=자격 증명에 실패하였습니다.
                        response.sendRedirect("/login");
                    }
                })
                .permitAll(); // .loginPage("/loginPage") 접근하는 경우 인증 받지 않도록 함
        return http.build();
    }
}
