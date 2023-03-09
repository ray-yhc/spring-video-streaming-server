package com.ray.hlsstreamingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// todo : security 개발 후 제거하기
@SpringBootApplication (exclude = SecurityAutoConfiguration.class)
public class HlsStreamingServerApplication {
    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }
    public static void main(String[] args) {
        SpringApplication.run(HlsStreamingServerApplication.class, args);
    }

}
