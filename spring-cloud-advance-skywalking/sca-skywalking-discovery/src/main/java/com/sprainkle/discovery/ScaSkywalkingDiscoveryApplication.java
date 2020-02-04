package com.sprainkle.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ScaSkywalkingDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScaSkywalkingDiscoveryApplication.class, args);
    }

}
