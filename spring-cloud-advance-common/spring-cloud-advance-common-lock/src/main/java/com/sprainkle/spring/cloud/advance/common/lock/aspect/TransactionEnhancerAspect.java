package com.sprainkle.spring.cloud.advance.common.lock.aspect;

import com.sprainkle.spring.cloud.advance.common.core.util.PointCutUtils;
import com.sprainkle.spring.cloud.advance.common.lock.annotation.DistributedLock;
import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockTemplate;
import com.sprainkle.spring.cloud.advance.common.lock.api.UnlockFailureProcessor;
import com.sprainkle.spring.cloud.advance.common.lock.context.DistributedLockContext;
import com.sprainkle.spring.cloud.advance.common.lock.context.DistributedLockContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 事务切面增强器.
 *
 * @author sprainkle
 * @date 2021/8/13
 */
@Slf4j
@Aspect
public class TransactionEnhancerAspect {

    private final DistributedLockTemplate distributedLockTemplate;

    public TransactionEnhancerAspect(DistributedLockTemplate distributedLockTemplate) {
        this.distributedLockTemplate = distributedLockTemplate;
    }

    @Before(value = "@annotation(transactional)")
    public void doBefore(JoinPoint jp, Transactional transactional) {

        // 是否开启了 可写的事务
        if (!isWithinWritableTransaction()) {
            return;
        }

        DistributedLock annotation = PointCutUtils.getAnnotation(jp, DistributedLock.class);
        if (annotation == null) {
            return;
        }

        DistributedLockContext context = DistributedLockContextHolder.getContext();
        Object lock = context.getLock();

        UnlockFailureProcessor unlockFailureProcessor = new UnlockFailureProcessor(distributedLockTemplate, lock);
        TransactionSynchronizationManager.registerSynchronization(unlockFailureProcessor);

    }

    /**
     * 是否在一个可写的事务中
     *
     * @return
     */
    private boolean isWithinWritableTransaction() {
        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        boolean isTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        return isTransactionActive && !isTransactionReadOnly;
    }

}
