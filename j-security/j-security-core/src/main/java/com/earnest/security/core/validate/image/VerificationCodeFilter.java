package com.earnest.security.core.validate.image;

import com.earnest.security.core.validate.ValidateCodeException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 定义验证码的过滤器
 */
@AllArgsConstructor
public class VerificationCodeFilter extends OncePerRequestFilter {

    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (StringUtils.equalsAnyIgnoreCase("/authentication/form", request.getRequestURI()) &&
                StringUtils.equalsAnyIgnoreCase("POST", request.getMethod())) {
            try {
                doValidate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void doValidate(ServletWebRequest request) throws ValidateCodeException, ServletRequestBindingException {
        HttpSession session = request.getRequest().getSession();
        ImageVerificationCode imageVerificationCode = (ImageVerificationCode) session.getAttribute(ImageVerificationCodeController.SESSION_KEY);
        String imageCode = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
        if (StringUtils.isBlank(imageCode)) {
            throw new ValidateCodeException("verification code empty or null");
        }
        if (imageVerificationCode == null) {
            throw new ValidateCodeException("verification code does not exist");
        }
        if (imageVerificationCode.getExpireTime().isBefore(LocalDateTime.now())) {
            session.removeAttribute(ImageVerificationCodeController.SESSION_KEY);
            throw new ValidateCodeException("verification code has expired");
        }
        if (!StringUtils.equalsAnyIgnoreCase(imageVerificationCode.getCode(), imageCode)) {
            throw new ValidateCodeException("verification code does not match");
        }
        session.removeAttribute(ImageVerificationCodeController.SESSION_KEY);
    }
}
