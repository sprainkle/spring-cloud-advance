package com.sprainkle.spring.cloud.advance.common.lock.api;


import com.sprainkle.spring.cloud.advance.common.lock.annotation.DistributedLock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁具体实现的模板接口
 *
 * @author sprainkle
 * @date 2019.04.20
 */
public interface DistributedLockTemplate {

    /** 尝试获取锁的默认等待时间 */
    long DEFAULT_WAIT_TIME = DistributedLock.DEFAULT_WAIT_TIME;
    /** 锁的默认超时时间. 超时后, 锁会被自动释放 */
    long DEFAULT_TIMEOUT = DistributedLock.DEFAULT_WAIT_TIME;
    /** 时间单位。默认为毫秒。 */
    TimeUnit DEFAULT_TIME_UNIT = DistributedLock.DEFAULT_TIME_UNIT;
    /** 获得锁名时拼接前后缀用到的分隔符 */
    String DEFAULT_SEPARATOR = DistributedLock.DEFAULT_SEPARATOR;
    /** lockName后缀 */
    String LOCK = DistributedLock.LOCK;

    /**
     * 使用分布式锁，使用锁默认超时时间。
     *
     * @param callback
     * @param fairLock 是否使用公平锁
     * @return
     */
    <T> T lock(DistributedLockCallback<T> callback, boolean fairLock);

    /**
     * 使用分布式锁。自定义锁的超时时间
     *
     * @param callback
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param timeUnit
     * @param fairLock  是否使用公平锁
     * @return
     */
    <T> T lock(DistributedLockCallback<T> callback, long leaseTime, TimeUnit timeUnit, boolean fairLock);

    /**
     * 尝试分布式锁，使用锁默认等待时间、超时时间。
     *
     * @param callback
     * @param <T>
     * @param fairLock 是否使用公平锁
     * @return
     */
    <T> T tryLock(DistributedLockCallback<T> callback, boolean fairLock);

    /**
     * 尝试分布式锁，自定义等待时间、超时时间。
     *
     * @param callback
     * @param waitTime  获取锁最长等待时间
     * @param leaseTime 锁超时时间。超时后自动释放锁。
     * @param timeUnit
     * @param <T>
     * @param fairLock  是否使用公平锁
     * @return
     */
    <T> T tryLock(DistributedLockCallback<T> callback, long waitTime, long leaseTime, TimeUnit timeUnit, boolean fairLock);

    /**
     * 锁是否由当前线程持有
     *
     * @param lock
     * @return
     */
    boolean isHeldByCurrentThread(Object lock);

}
