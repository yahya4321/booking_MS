package com.CheritSolutions.Booking_Microservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            JwtAuthenticationToken token = (JwtAuthenticationToken) 
                SecurityContextHolder.getContext().getAuthentication();
            if (token != null) {
                String jwt = token.getToken().getTokenValue();
                System.out.println("Propagating JWT: " + jwt); // Debug log
                template.header("Authorization", "Bearer " + jwt);
            } else {
                System.err.println("No JWT found in SecurityContext!"); // Error log
            }
        };
    }
}