package com.earnest.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.earnest")
public class VideoWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoWebApplication.class, args);
    }

}
