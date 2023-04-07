package com.study.security.coresecurity.provider;

import com.study.security.coresecurity.common.FormWebAuthenticationDetails;
import com.study.security.coresecurity.token.AjaxAuthenticationToken;
import com.study.security.service.AccountContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * 실제적인 인증 처리를 하는 클래스
 */
@Slf4j
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Authentication 객체에는 사용자가 로그인시 입력한 id, pw 정보가 담긴다
    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("===AjaxAuthenticationProvider authenticate()===");

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // db에 저장된 유저 정보
        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("BadCredentialsException!!!");
        }

        // id, password 외에도 secret_key에 대한 검증도 한다.
        FormWebAuthenticationDetails formWebAuthenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = formWebAuthenticationDetails.getSecretKey();
        if (secretKey == null || !"secret".equals(secretKey)) {
            throw new InsufficientAuthenticationException("InsufficientAuthenticationException!!!");
        }

        // 인증에 성공한 인증 정보를 토큰객체로 만들어서 리턴하게 한다.
        return new AjaxAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AjaxAuthenticationToken.class);
    }
}
