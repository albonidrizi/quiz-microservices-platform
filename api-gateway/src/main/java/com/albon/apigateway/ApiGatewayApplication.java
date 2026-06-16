package com.albon.apigateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> {
			String forwardedFor = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
			if (forwardedFor != null && !forwardedFor.isBlank()) {
				return Mono.just(forwardedFor.split(",")[0].trim());
			}

			InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
			String key = remoteAddress == null ? "unknown" : remoteAddress.getAddress().getHostAddress();
			return Mono.just(key);
		};
	}

}
