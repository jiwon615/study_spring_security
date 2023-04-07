package com.study.security.coresecurity.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("===CustomAuthenticationFailureHandler onAuthenticationFailure()===");
        String errorMessage = "Invalid Username or Password!!";
        if (exception instanceof BadCredentialsException) {
        } else if (exception instanceof InsufficientAuthenticationException) {
            errorMessage = "Invalid Secret Key!!";
        }

        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage); // 하나의 url 문자열로 인식
        super.onAuthenticationFailure(request, response, exception);
    }
}
