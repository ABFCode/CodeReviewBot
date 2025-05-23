package com.github.abfcode.codereviewbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Configuration
public class AppConfig {

    @Value("${bitbucket.api.baseUrl}")
    private String baseUrl;

    @Value("${bitbucket.username}")
    private String username;

    @Value("${bitbucket.app.password}")
    private String appPassword;

    @Bean
    public RestClient bitBucketRestClient() {
        String auth = username + ":" + appPassword;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, authHeader)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }
}
