package com.sprainkle.spring.cloud.advance.common.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sprainkle
 * @date 2021/8/14
 */
@SpringBootApplication
public class DistributedLockTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedLockTestApplication.class, args);
    }

}
