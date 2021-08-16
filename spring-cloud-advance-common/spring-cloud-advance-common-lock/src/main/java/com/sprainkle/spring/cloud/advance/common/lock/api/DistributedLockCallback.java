package com.sprainkle.spring.cloud.advance.common.lock.api;

/**
 * 分布锁回调接口
 *
 * Created by sprainkle on 2017/11/14.
 */
public interface DistributedLockCallback<T> {

    /**
     * 调用者必须在此方法中实现需要加分布式锁的业务逻辑
     *
     * @return
     */
    public T process() throws Throwable;

    /**
     * 得到分布式锁名称
     *
     * @return
     */
    public String getLockName();

}
