package com.sprainkle.ueh.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UehOrganizationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UehOrganizationApplication.class, args);
	}

}
