package com.albon.questionservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class InternalApiKeyInterceptor implements HandlerInterceptor {

    private static final String HEADER_NAME = "X-Internal-Api-Key";
    private final byte[] expectedApiKey;

    public InternalApiKeyInterceptor(@Value("${app.internal-api-key}") String expectedApiKey) {
        this.expectedApiKey = expectedApiKey.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String suppliedApiKey = request.getHeader(HEADER_NAME);
        boolean valid = suppliedApiKey != null
                && MessageDigest.isEqual(expectedApiKey, suppliedApiKey.getBytes(StandardCharsets.UTF_8));

        if (!valid) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid internal API key");
        }
        return valid;
    }
}
