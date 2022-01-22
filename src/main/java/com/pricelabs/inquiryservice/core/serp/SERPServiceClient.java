package com.pricelabs.inquiryservice.core.serp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

public class SERPServiceClient {

    private final WebClient webClient;

    public SERPServiceClient(@Value("${classic.base.url}") String baseUrl,
                             @Value("${classic.api.username}") String userName,
                             @Value("${classic.api.password}") String password) {
        this.webClient = WebClient.builder().baseUrl(baseUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.setBasicAuth(userName, password))
                .build();
    }


}
