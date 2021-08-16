package com.sprainkle.spring.cloud.advance.common.lock.context;

/**
 *
 *
 * @author sprainkle
 * @date 2021/8/13
 */
public class DistributedLockContext {

    private Object lock;

    public void setLock(Object lock) {
        this.lock = lock;
    }

    public Object getLock() {
        return lock;
    }

}
