package com.albon.questionservice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InternalApiKeyInterceptorTest {

    private final InternalApiKeyInterceptor interceptor = new InternalApiKeyInterceptor("test-key");

    @Test
    void preHandleRejectsMissingApiKey() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertFalse(allowed);
        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid internal API key");
    }

    @Test
    void preHandleAcceptsMatchingApiKey() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("X-Internal-Api-Key")).thenReturn("test-key");

        boolean allowed = interceptor.preHandle(request, response, new Object());

        assertTrue(allowed);
    }
}
