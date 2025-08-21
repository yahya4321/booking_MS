package com.CheritSolutions.Booking_Microservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // 2. Disable CSRF for stateless API
            .csrf(csrf -> csrf.disable())
            // 3. Use stateless session management
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 4. Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Allow OPTIONS requests for CORS preflight
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Require authentication for booking endpoints
                .requestMatchers("/api/v1/bookings/**").authenticated()
                // Allow error endpoint
                .requestMatchers("/error").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            // 5. Configure OAuth2 Resource Server for JWT validation
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(jwtAuthConverter)
                )
            );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withIssuerLocation("http://keycloak:8080/realms/Business-realm").build();
    }

    // 6. Define CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Allow credentials (e.g., Authorization header)
        // Specify allowed origins (match your frontend and other clients)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",
            "http://frontend-app:80",
            "http://business-app:8083",
            "https://booking.guru2023.uk"
        ));
        // Allow necessary HTTP methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        // Allow all headers (including Authorization)
        config.setAllowedHeaders(Collections.singletonList("*"));
        // Expose headers if needed by the frontend
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // Apply CORS to all paths
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}