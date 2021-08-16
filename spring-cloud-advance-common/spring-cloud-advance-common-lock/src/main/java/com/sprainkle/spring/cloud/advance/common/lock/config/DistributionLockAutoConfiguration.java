package com.sprainkle.spring.cloud.advance.common.lock.config;

import cn.hutool.core.util.StrUtil;
import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockTemplate;
import com.sprainkle.spring.cloud.advance.common.lock.api.impl.RedisDistributedLockTemplate;
import com.sprainkle.spring.cloud.advance.common.lock.api.impl.ZooDistributedLockTemplate;
import com.sprainkle.spring.cloud.advance.common.lock.aspect.DistributedLockAspect;
import com.sprainkle.spring.cloud.advance.common.lock.aspect.TransactionEnhancerAspect;
import com.sprainkle.spring.cloud.advance.common.lock.property.DistributedLockProperties;
import com.sprainkle.spring.cloud.advance.common.lock.property.RedisProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/04/20
 */
@Configuration
@ConditionalOnClass({DistributedLockAspect.class})
@ConditionalOnProperty(prefix = "distributed.lock", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({DistributedLockProperties.class})
public class DistributionLockAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    DistributedLockTemplate distributedLockTemplate(DistributedLockProperties properties, RedissonClient redissonClient)
            throws Exception {
        if(properties.getImpl().equals("zoo")) {
            return zooDistributedLockTemplate(properties);
        }
        return redisDistributedLockTemplate(properties, redissonClient);
    }

    @Bean
    @ConditionalOnMissingBean
    DistributedLockAspect distributedLockAspect(DistributedLockTemplate distributedLockTemplate) {
        return new DistributedLockAspect(distributedLockTemplate);
    }

    @Bean
    TransactionEnhancerAspect transactionEnhancerAspect(DistributedLockTemplate distributedLockTemplate) {
        return new TransactionEnhancerAspect(distributedLockTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    RedissonClient redissonClient(DistributedLockProperties properties) {
        RedisProperties redisConfig = properties.getRedis();
        if (redisConfig == null) {
            return Redisson.create();
        }
        String host = StringUtils.hasText(redisConfig.getHost()) ? redisConfig.getHost() : "127.0.0.1";
		String password = StrUtil.isBlank(redisConfig.getPassword()) ? null : redisConfig.getPassword();
        String port = StringUtils.hasText(redisConfig.getPort()) ? redisConfig.getPort() : "6379";
		RedisProperties.Sentinel sentinel = properties.getRedis().getSentinel();

		Config config = new Config();

		if (sentinel == null) {
			config.useSingleServer()
				.setAddress("redis://" + host + ":" + port)
				.setPassword(password)
                .setPingConnectionInterval(30000);
		} else {
			// 哨兵
			SentinelServersConfig sentinelServersConfig = config.useSentinelServers();
			sentinelServersConfig.setPassword(password);
			sentinelServersConfig.setMasterName(sentinel.getMaster());
            sentinelServersConfig.setPingConnectionInterval(30000);

			for (String node : sentinel.getNodes()) {
				sentinelServersConfig.addSentinelAddress("redis://" + node);
			}

		}

		return Redisson.create(config);
    }

    RedisDistributedLockTemplate redisDistributedLockTemplate(DistributedLockProperties properties,
                                                              RedissonClient redissonClient) {
        if (redissonClient == null) {
            redissonClient = redissonClient(properties);
        }
        return new RedisDistributedLockTemplate(redissonClient, properties);
    }

    ZooDistributedLockTemplate zooDistributedLockTemplate(DistributedLockProperties properties) throws Exception {
        return new ZooDistributedLockTemplate(properties);
    }
}
