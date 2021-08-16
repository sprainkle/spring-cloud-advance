package com.sprainkle.spring.cloud.advance.common.lock.context;

/**
 * @author sprainkle
 * @date 2021/8/13
 */
public class DistributedLockContextHolder {

    private static final ThreadLocal<DistributedLockContext> contextHolder = ThreadLocal.withInitial(DistributedLockContext::new);

    public static DistributedLockContext getContext() {
        return contextHolder.get();
    }

    /**
     * bind the given {@link DistributedLockContext} to the current thread.
     *
     * @param context
     */
    public static void setContext(DistributedLockContext context) {
        contextHolder.set(context);
    }

    /**
     * reset the current context.
     */
    public static void resetContext() {
        contextHolder.remove();
    }

}
