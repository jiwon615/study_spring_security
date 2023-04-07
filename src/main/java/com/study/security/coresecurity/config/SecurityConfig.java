package com.study.security.coresecurity.config;

import com.study.security.coresecurity.filter.AjaxLoginProcessingFilter;
import com.study.security.coresecurity.handler.CustomAccessDeniedHandler;
import com.study.security.coresecurity.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler; // CustomAuthenticationSuccessHandler 주입됨

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private AuthenticationDetailsSource authenticationDetailsSource;

    /**
     * UserDetailsService 의 구현체인 CustomUserDetailsService 사용하도록 등록
     */
    @Bean
    public AuthenticationManager configure(AuthenticationConfiguration auth) throws Exception {
        // AuthenticationManager 빈 생성 시 스프링의 내부 동작으로 인해 위에서 작성한 CustomUserDetailsService 구현체를 주입받아 자동으로 설정됨
        return auth.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적파일들은 포안필터 거치지 않고 통과되도록 함 (permitAll과 다른점은 보안필터를 거치고, 통과시키는것)
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 인가 처리
        http.authorizeRequests()
                .antMatchers("/", "/users", "user/login/**", "/login*").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc") // login.html에서 form태그 action="login_proc"과 동일하게 맞춰주어야 한다.
                .defaultSuccessUrl("/")
                .authenticationDetailsSource(authenticationDetailsSource)  // 인증 부가 기능 (사용자가 보내주는 파라미터 검증)
                .successHandler(customAuthenticationSuccessHandler) // 인증 성공 시 후속처리
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll(); // 로그인 화면은 인증 받지 않은 사용자도 접근 가능해야 함
        
        http
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());

        http.csrf().disable();

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
