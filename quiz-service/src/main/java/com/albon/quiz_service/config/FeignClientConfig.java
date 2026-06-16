package com.albon.quiz_service.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public RequestInterceptor internalApiKeyRequestInterceptor(@Value("${app.internal-api-key}") String apiKey) {
        return requestTemplate -> requestTemplate.header("X-Internal-Api-Key", apiKey);
    }
}
