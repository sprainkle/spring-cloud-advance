package com.sprainkle.spring.cloud.advance.common.lock.property;

import lombok.Data;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/04/20
 */
@Data
public class ZooProperties {
    /**
     * zookeeper服务器地址. 多个时用','分开
     */
    private String connectString;
    /**
     * zookeeper的session过期时间. 即锁的过期时间. 可用于全局配置锁的过期时间
     */
    private Long sessionTimeoutMs;
    /**
     * zookeeper的连接超时时间
     */
    private Long connectionTimeoutMs;

}
