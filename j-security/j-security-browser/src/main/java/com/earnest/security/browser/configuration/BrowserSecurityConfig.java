package com.earnest.security.browser.configuration;

import com.earnest.security.browser.authentacation.DefaultAuthenticationFailureHandler;
import com.earnest.security.browser.authentacation.DefaultAuthenticationSuccessHandler;
import com.earnest.security.core.properties.SecurityProperties;
import com.earnest.security.core.validate.image.VerificationCodeFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SecurityProperties securityProperties;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    public BrowserSecurityConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
        this.authenticationFailureHandler = new DefaultAuthenticationFailureHandler(securityProperties);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new VerificationCodeFilter(authenticationFailureHandler), UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/authentication/require")//自定义跳转页面
                .loginProcessingUrl("/authentication/form")//登陆表单提交的请求
                .successHandler(new DefaultAuthenticationSuccessHandler(securityProperties))
                .failureHandler(authenticationFailureHandler)
                .and()
                .authorizeRequests()//对请求做授权
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/image"
                ).permitAll()//允许访问
                .anyRequest()//任何请求
                .authenticated()//都需要认证
                .and()
                .csrf().disable();//禁用csrf的防护
    }
}

