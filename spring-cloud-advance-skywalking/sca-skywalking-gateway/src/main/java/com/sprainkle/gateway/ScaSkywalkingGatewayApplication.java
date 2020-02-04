package com.sprainkle.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ScaSkywalkingGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaSkywalkingGatewayApplication.class, args);
	}

}
