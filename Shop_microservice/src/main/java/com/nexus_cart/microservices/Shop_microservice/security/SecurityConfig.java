package com.nexus_cart.microservices.Shop_microservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // Public read access for store catalog and documentation
                    .requestMatchers(HttpMethod.GET, "/products/**", "/api/products/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    
                    // Admin-only operations for product catalog mutations
                    .requestMatchers(HttpMethod.POST, "/products", "/api/products").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/products/*", "/api/products/*").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/products/delete", "/api/products/delete").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/products/*", "/api/products/*").hasRole("ADMIN")
                    
                    .anyRequest().authenticated()
            );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}