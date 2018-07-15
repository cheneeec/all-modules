package com.earnest.security.browser.authentacation;

import com.alibaba.fastjson.JSONObject;
import com.earnest.security.core.properties.BrowserProperties;
import com.earnest.security.core.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
public class DefaultAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (securityProperties.getBrowser().getLoginType().equals(BrowserProperties.LoginType.JSON)) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSONObject.toJSONString(Collections.singletonMap("message", exception.getMessage())));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
