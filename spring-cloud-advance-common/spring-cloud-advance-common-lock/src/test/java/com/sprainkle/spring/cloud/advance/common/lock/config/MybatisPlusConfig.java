package com.sprainkle.spring.cloud.advance.common.lock.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author sprainkle
 * @date 2021/8/14
 */
@Configuration
@MapperScan({"com.sprainkle.spring.cloud.advance.common.lock.mapper"})
public class MybatisPlusConfig {

}
