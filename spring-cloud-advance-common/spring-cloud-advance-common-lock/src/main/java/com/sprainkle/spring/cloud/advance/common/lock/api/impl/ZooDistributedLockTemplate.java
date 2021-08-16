package com.sprainkle.spring.cloud.advance.common.lock.api.impl;

import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockCallback;
import com.sprainkle.spring.cloud.advance.common.lock.context.DistributedLockContext;
import com.sprainkle.spring.cloud.advance.common.lock.context.DistributedLockContextHolder;
import com.sprainkle.spring.cloud.advance.common.lock.enums.ResponseEnum;
import com.sprainkle.spring.cloud.advance.common.lock.property.DistributedLockProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <pre>
 *      基于zookeeper实现的分布式锁
 * </pre>
 *
 * @author sprainkle
 * @date 2019/04/20
 */
@Slf4j
public class ZooDistributedLockTemplate extends AbstractDistributedLockTemplate {

    public final static long DEFAULT_CONNECTION_TIMEOUT = 15000L;

    private CuratorFramework client;
    /** zookeeper连接状态读写锁 */
    private ReadWriteLock zkClientStateLock = new ReentrantReadWriteLock();
    /** zookeeper连接状态 */
    private ConnectionState connState;

    private final String namespace;
    private final String connectString;
    private final long sessionTimeoutMs;
    private final long waitTimeoutMs;
    private final long connectionTimeoutMs;

    // 锁前缀
    private final String lockPrefix;
    // 锁后缀
    private final String lockPostfix;

    /**
     * @param properties
     */
    public ZooDistributedLockTemplate(DistributedLockProperties properties) throws Exception {

        this.namespace = properties.getNamespace();
        this.connectString = Optional.ofNullable(properties.getZoo().getConnectString()).orElse("127.0.0.1:2181");
        this.sessionTimeoutMs = Optional.ofNullable(properties.getLockTimeoutMs()).orElse(DEFAULT_TIMEOUT);
        this.waitTimeoutMs = Optional.ofNullable(properties.getWaitTimeoutMs()).orElse(DEFAULT_WAIT_TIME);
        this.connectionTimeoutMs = Optional.ofNullable(properties.getZoo().getConnectionTimeoutMs()).orElse(DEFAULT_CONNECTION_TIMEOUT);

        this.client = getClient();

        this.lockPrefix = namespace + DEFAULT_SEPARATOR;
        this.lockPostfix = ".lock";
    }

    private CuratorFramework getClient() {
        CuratorFramework client = getClient(this.sessionTimeoutMs);
        //client.getZookeeperClient().blockUntilConnectedOrTimedOut();
        return client;
    }

    private CuratorFramework getClient(long sessionTimeoutMs) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 2);
        CuratorFramework client =
                CuratorFrameworkFactory
                        .builder()
                        .connectString(connectString)
                        .sessionTimeoutMs((int) sessionTimeoutMs)
                        .connectionTimeoutMs((int) connectionTimeoutMs)
                        .retryPolicy(retryPolicy)
                        .namespace(namespace + "/distributed/lock")
                        .build();
        client.getConnectionStateListenable().addListener(connectionStateListener());
        client.start();
        return client;
    }

    /**
     * 使用分布式锁，使用锁默认超时时间。
     *
     * @param callback
     * @param fairLock 是否使用公平锁
     * @return
     */
    @Override
    public <T> T lock(DistributedLockCallback<T> callback, boolean fairLock) {
        return lock(callback, sessionTimeoutMs, DEFAULT_TIME_UNIT, fairLock);
    }

    /**
     * 使用分布式锁。自定义锁的超时时间
     *
     * @param callback
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param timeUnit
     * @param fairLock  是否使用公平锁
     * @return
     */
    @Override
    public <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        CuratorFramework client = this.client;
        if (sessionTimeoutMs != leaseTime && DEFAULT_TIME_UNIT != timeUnit) {
            long sessionTimeout = timeUnit.toSeconds(leaseTime);
            if (this.sessionTimeoutMs != sessionTimeout) {
                throw new UnsupportedOperationException("暂不支持自定义锁超时时间");
                //client = getClient(sessionTimeout);
            }
        }

        checkAvailable();
        InterProcessMutex lock = getLock(client, callback.getLockName());

        DistributedLockContext context = DistributedLockContextHolder.getContext();
        context.setLock(lock);

        try {
            lock(lock);
            return process(callback);
        } finally {
            unlock(lock);
        }
    }

    /**
     * 尝试分布式锁，使用锁默认等待时间、超时时间。
     *
     * @param callback
     * @param fairLock 是否使用公平锁
     * @return
     */
    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock) {
        return tryLock(callback, waitTimeoutMs, sessionTimeoutMs, DEFAULT_TIME_UNIT, fairLock);
    }

    /**
     * 尝试分布式锁，自定义等待时间、超时时间。
     *
     * @param callback
     * @param waitTime  获取锁最长等待时间
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param timeUnit 时间单位
     * @param fairLock  是否使用公平锁
     * @return
     */
    @Override
    public <T> T tryLock(DistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock) {
        CuratorFramework client = this.client;
        if (sessionTimeoutMs != leaseTime && DEFAULT_TIME_UNIT != timeUnit) {
            long sessionTimeout = timeUnit.toSeconds(leaseTime);
            if (this.sessionTimeoutMs != sessionTimeout) {
                throw new UnsupportedOperationException("暂不支持自定义锁超时时间");
                //client = getClient(sessionTimeout);
            }
        }

        checkAvailable();
        InterProcessMutex lock = getLock(client, callback.getLockName());

        DistributedLockContext context = DistributedLockContextHolder.getContext();
        context.setLock(lock);

        try {
            lock(lock, waitTime, timeUnit);
            return process(callback);
        } finally {
            unlock(lock);
        }
    }

    /**
     * 获取{@link InterProcessMutex}锁对象
     * @param client
     * @param lockName
     * @return
     */
    private InterProcessMutex getLock(CuratorFramework client, String lockName) {
        lockName = lockPrefix + lockName + lockPostfix;
        InterProcessMutex lock = new InterProcessMutex(client, "/" + lockName);
        return lock;
    }

    /**
     * 阻塞获取锁. 当zookeeper连接异常时, 获取失败, 抛异常.
     *
     * @param lock
     */
    private void lock(InterProcessMutex lock) {

        try {
            lock.acquire();
        } catch (Exception e) {
            ResponseEnum.LOCK_NOT_YET_HOLD.assertFailWithMsg("获取锁失败. 可能原因: zookeeper 连接异常", e);
        }
    }

    /**
     * 在指定的时间内尝试获取锁, 若无法获取, 抛异常.
     *
     * @param lock
     * @param waitTime
     * @param timeUnit
     */
    private void lock(InterProcessMutex lock, long waitTime, TimeUnit timeUnit) {
        boolean acquired = false;
        try {
            log.debug("尝试获取锁...");
            acquired = lock.acquire(waitTime, timeUnit);
        } catch (Exception e) {
            ResponseEnum.LOCK_NOT_YET_HOLD.assertFailWithMsg("尝试获取锁失败", e);
        }
        if (acquired) {
            log.debug("获得锁.");
        } else {
            ResponseEnum.LOCK_NOT_YET_HOLD.assertFailWithMsg("尝试获取锁超时, 获取失败.");
        }
    }

    /**
     * 释放锁
     * @param lock
     */
    private void unlock(InterProcessMutex lock) {
        checkAvailable();
        try {
            log.debug("判断锁是否仍由当前线程持有...");
            if (lock.isAcquiredInThisProcess()) {
                log.debug("锁由当前线程持有, 正在释放...");
                lock.release();
                log.debug("锁释放成功.");
            }
        } catch (Exception e) {
            ResponseEnum.LOCK_NO_MORE_HOLD.assertFailWithMsg("释放锁失败", e);
        }
    }

    /**
     * 校验zookeeper连接是否可用
     */
    private void checkAvailable() {
        log.debug("检查zookeeper连接是否可用...");
        Lock readLock = zkClientStateLock.readLock();
        try {
            readLock.lock();
            if (!this.connState.isConnected()) {
                ResponseEnum.LOCK_NOT_YET_HOLD.assertFailWithMsg("zookeeper连接不可用");
            }
            log.debug("zookeeper连接正常");
        } finally {
            readLock.unlock();
        }
    }

    private ConnectionStateListener connectionStateListener() {
        return (CuratorFramework oldClient, ConnectionState newState) -> {
            log.debug("zookeeper连接状态变更. 当前状态: {}", newState);

            Lock writeLock = zkClientStateLock.writeLock();
            try {
                writeLock.lock();
                connState = newState;
            } finally {
                writeLock.unlock();
            }

            if (newState == ConnectionState.SUSPENDED) {
                log.error("zookeeper连接挂起.");
            }
            if (newState == ConnectionState.LOST) {
                log.error("zookeeper连接断开.");
                // 当连接断开, 则关闭连接. 因为连接断开后, 所有curator的api都不可用, 而且还会一直阻塞, 直到重新连接.
                oldClient.close();

                client = getClient();
            }
        };
    }

    @Override
    public boolean isHeldByCurrentThread(Object lock) {
        if (!(lock instanceof InterProcessMutex)) {
            return false;
        }

        return ((InterProcessMutex) lock).isAcquiredInThisProcess();
    }
}
