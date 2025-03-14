package com.example.myApp.filter.config;

import com.example.myApp.filter.APIKeyAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
@EnableWebSecurity
public class ApiFilterConfig {

    @Value("${auth-token-header-name}")
    private String principalRequestHeader;

    @Value("${auth-token}")
    private ArrayList<String> principalRequestValue;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        APIKeyAuthFilter filter = new APIKeyAuthFilter(principalRequestHeader);
        filter.setAuthenticationManager(authentication -> {
            String principal = authentication.getPrincipal().toString();
            if (!principalRequestValue.contains(principal)) {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }
            authentication.setAuthenticated(true);
            return authentication;
        });
        return httpSecurity
                .cors(Customizer.withDefaults()) // Enables CORS
                .csrf(csrf -> csrf.disable()) // Disables CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session
                .addFilter(filter)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/api/v1/reviews/health-check").permitAll()
                        .requestMatchers("/api/v1/property-review/**").permitAll()
                        .requestMatchers("/api/v1/rev-pilot/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}