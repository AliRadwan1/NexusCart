package com.nexus_cart.microservices.api_gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Wallet Service routes
                .route("wallet_service", r -> r.path("/users/**", "/wallets/**", "/transactions/**")
                        .uri("lb://wallet-microservice"))

                // Shop Service routes
                .route("shop_service", r -> r.path("/api/products/**", "/api/cart/**", "/api/orders/**")
                        .uri("lb://shop-microservice"))

                // Inventory Service routes
                .route("inventory_service", r -> r.path("/api/inventory/**")
                        .uri("lb://inventory-microservice"))
                .build();
    }
}