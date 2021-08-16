package com.sprainkle.spring.cloud.advance.common.lock.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/04/20
 */
@Data
@ConfigurationProperties("sca-common.distributed.lock")
public class DistributedLockProperties {

    private Boolean enabled;

    private String namespace;

    private String impl;

    /**
     * 锁全局过期时间
     */
    private Long lockTimeoutMs;
    /**
     * 尝试锁全局等待时间
     */
    private Long waitTimeoutMs;

    /**
     * 服务名
     */
    @Value("${spring.application.name}")
    private String serviceName;

    private ZooProperties zoo;

    private RedisProperties redis;

    public String getNamespace() {
        if (!StringUtils.hasText(namespace)) {
            if (!StringUtils.hasText(this.serviceName)) {
                throw new IllegalArgumentException("serviceName或namespace至少有一个不为空.");
            }
            this.namespace = this.serviceName;
        }
        return namespace;
    }

}
