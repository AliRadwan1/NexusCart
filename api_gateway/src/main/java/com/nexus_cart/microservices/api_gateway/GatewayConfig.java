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
                .route("wallet_service", r -> r.path("/api/wallets/**")
                        .uri("lb://wallet_microservice"))
                .route("inventory_service", r -> r.path("/api/inventory/**")
                        .uri("lb://inventory_microservice"))
                .route("shop_service", r -> r.path("/api/products/**")
                        .uri("lb://Shop_microservice"))
                .build();
    }
}