package com.nexus_cart.microservices.Shop_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ShopMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopMicroserviceApplication.class, args);
	}

}
