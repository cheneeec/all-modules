package com.earnest.security.browser.authentacation;

import com.alibaba.fastjson.JSONObject;
import com.earnest.security.core.properties.BrowserProperties;
import com.earnest.security.core.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 自定义登陆成功处理
 */
@Slf4j
@AllArgsConstructor
public class DefaultAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("User:[{}] successfully logged in at {}", ((UserDetails) authentication.getPrincipal()).getUsername(), LocalDateTime.now());
        if (BrowserProperties.LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONObject.toJSONString(authentication));
            return;
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
