package com.study.security.controller.login;

import com.study.security.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception, Model model) {
        log.info("===/login===");
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "user/login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("===/logout===");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // SecurityContextLogoutHandler 는 로그아웃 처리 하는 핸들러
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication); // 로그아웃 처리
        }
        return "redirect:/login";
    }

    @GetMapping("/denied")
    public String denied(@RequestParam(value = "exception", required = false) String exception, Model model) {
        log.info("===/denied===");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account account = (Account) authentication.getPrincipal();
        model.addAttribute("username", account.getUsername());
        model.addAttribute("exception", exception);
        return "user/login/denied";
    }
}
