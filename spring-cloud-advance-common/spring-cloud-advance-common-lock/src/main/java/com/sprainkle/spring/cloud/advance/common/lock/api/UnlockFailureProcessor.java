package com.sprainkle.spring.cloud.advance.common.lock.api;

import com.sprainkle.spring.cloud.advance.common.lock.enums.ResponseEnum;
import org.springframework.transaction.support.TransactionSynchronization;

/**
 * 锁释放失败时的处理器. 如果当前线程不是锁的持有者, 直接抛异常让数据回滚
 *
 * @author sprainkle
 * @date 2021/8/13
 */
public class UnlockFailureProcessor implements TransactionSynchronization {

    private final Object lock;

    private final DistributedLockTemplate distributedLockTemplate;

    public UnlockFailureProcessor(DistributedLockTemplate distributedLockTemplate, Object lock) {
        this.distributedLockTemplate = distributedLockTemplate;
        this.lock = lock;
    }

    @Override
    public void beforeCommit(boolean readOnly) {
        boolean heldByCurrentThread = distributedLockTemplate.isHeldByCurrentThread(lock);

        if (!heldByCurrentThread) {
            ResponseEnum.LOCK_NO_MORE_HOLD.assertFailWithMsg("释放锁时, 当前线程不是锁的持有者");
        }
    }

}
