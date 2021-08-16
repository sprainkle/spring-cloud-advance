package com.sprainkle.spring.cloud.advance.common.lock.aspect;

import java.lang.reflect.Method;

/**
 * Class describing the root object used during the expression evaluation.
 *
 * @author sprainkle
 * @date 2021/4/26
 */
public class DistributedLockExpressionRootObject {
    private final Method method;

    private final Object[] args;

    private final Object target;

    private final Class<?> targetClass;

    public DistributedLockExpressionRootObject(Method method, Object[] args, Object target, Class<?> targetClass) {
        this.method = method;
        this.args = args;
        this.target = target;
        this.targetClass = targetClass;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getTarget() {
        return target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}
