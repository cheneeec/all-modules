package com.earnest.security.core.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrowserProperties {

    private String loginPage = "/default-signIn.html";
    private LoginType loginType = LoginType.JSON;

    public enum LoginType {
        JSON,
        REDIRECT
    }
}
