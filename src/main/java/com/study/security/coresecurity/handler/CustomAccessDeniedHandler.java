package com.study.security.coresecurity.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 인가예외 처리 핸들리
 */
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private String errorPage; // /denied 받게 됨

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("===CustomAccessDeniedHandler handle()===");
        String deniedUrl = errorPage + "?exception=" + accessDeniedException.getMessage();
        response.sendRedirect(deniedUrl);
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }
}
