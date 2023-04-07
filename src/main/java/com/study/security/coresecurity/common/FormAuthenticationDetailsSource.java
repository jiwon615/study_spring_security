package com.study.security.coresecurity.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * FormWebAuthenticationDetails 클래스를 생성
 */
@Component
@Slf4j
public class FormAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        log.info("===FormAuthenticationDetailsSource buildDetails()===");
        return new FormWebAuthenticationDetails(context);
    }
}
