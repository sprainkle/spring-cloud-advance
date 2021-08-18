package com.sprainkle.spring.cloud.advance.common.lock.api.impl;


import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockCallback;
import com.sprainkle.spring.cloud.advance.common.lock.context.DistributedLockContext;
import com.sprainkle.spring.cloud.advance.common.lock.context.DistributedLockContextHolder;
import com.sprainkle.spring.cloud.advance.common.lock.enums.ResponseEnum;
import com.sprainkle.spring.cloud.advance.common.lock.property.DistributedLockProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *  基于Redisson实现的分布式锁
 * </pre>
 *
 * @author sprainkle
 * @date 2019/04/20
 */
@Slf4j
public class RedisDistributedLockTemplate extends AbstractDistributedLockTemplate {

    private final RedissonClient redisson;
    private final String namespace;

    private final long lockTimeoutMs;
    private final long waitTimeoutMs;

    // 锁前缀
    private final String lockPrefix;
    // 锁后缀
    private final String lockPostfix;

    public RedisDistributedLockTemplate(RedissonClient redisson, DistributedLockProperties properties) {
        this.redisson = redisson;
        this.namespace = properties.getNamespace();
        this.lockTimeoutMs = Optional.ofNullable(properties.getLockTimeoutMs()).orElse(DEFAULT_TIMEOUT);
        this.waitTimeoutMs = Optional.ofNullable(properties.getWaitTimeoutMs()).orElse(DEFAULT_WAIT_TIME);

        this.lockPrefix = namespace + DEFAULT_SEPARATOR;
        this.lockPostfix = ".lock";
    }

    @Override
    public <T> T lock(DistributedLockCallback<T> callback, boolean fairLock) {
        return lock(callback, lockTimeoutMs, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);

        DistributedLockContext context = DistributedLockContextHolder.getContext();
        context.setLock(lock);

        try {
            lock.lock(leaseTime, timeUnit);
            return process(callback);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock) {
        return tryLock(callback, waitTimeoutMs, lockTimeoutMs, DEFAULT_TIME_UNIT, fairLock);
    }

    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback,
                         long waitTime,
                         long leaseTime,
                         TimeUnit timeUnit,
                         boolean fairLock) {
        RLock lock = getLock(callback.getLockName(), fairLock);
        boolean locked = false;

        DistributedLockContext context = DistributedLockContextHolder.getContext();
        context.setLock(lock);

        try {
            locked = lock.tryLock(waitTime, leaseTime, timeUnit);
            if (locked) {
                return process(callback);
            }
        } catch (InterruptedException e) {
            ResponseEnum.LOCK_NOT_YET_HOLD.assertFailWithMsg("无法在指定时间内获得锁", e);
        } finally {
            // 是否未获得锁
            if (!locked) {
                ResponseEnum.LOCK_NOT_YET_HOLD.assertFailWithMsg("尝试获取锁超时, 获取失败.");
            }

            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            } else {
                log.warn("锁释放失败, 当前线程不是锁的持有者");
            }
        }
        return null;
    }

    public RLock getLock(String lockName, boolean fairLock) {
        RLock lock;
        lockName = lockPrefix + lockName + lockPostfix;
        if (fairLock) {
            lock = redisson.getFairLock(lockName);
        } else {
            lock = redisson.getLock(lockName);
        }
        return lock;
    }

    @Override
    public boolean isHeldByCurrentThread(Object lock) {
        if (!(lock instanceof RLock)) {
            return false;
        }

        return ((RLock) lock).isHeldByCurrentThread();
    }
}
