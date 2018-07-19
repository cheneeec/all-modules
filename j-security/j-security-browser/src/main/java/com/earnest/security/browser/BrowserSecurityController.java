package com.earnest.security.browser;

import com.earnest.security.core.properties.SecurityProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * 控制身份认证时的跳转。
 */
@RestController
@Slf4j
@AllArgsConstructor
public class BrowserSecurityController {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final SecurityProperties securityProperties;


    @GetMapping("/authentication/require")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Map<String, Object> requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (Objects.nonNull(savedRequest)) {
            String targetUrl = savedRequest.getRedirectUrl();
            log.info("When accessing {}, the page jumps", targetUrl);
            //引发跳转的url是否是.html结尾
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                //如果是html请求则跳转到此页面
                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        //其它则返回json
        return Collections.singletonMap("message", "Access to content requires authentication");

    }


}
