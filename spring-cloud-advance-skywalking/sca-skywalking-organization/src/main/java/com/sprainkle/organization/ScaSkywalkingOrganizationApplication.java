package com.sprainkle.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ScaSkywalkingOrganizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScaSkywalkingOrganizationApplication.class, args);
	}

}
