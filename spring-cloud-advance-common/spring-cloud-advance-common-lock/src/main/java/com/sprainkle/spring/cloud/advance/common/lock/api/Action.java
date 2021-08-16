package com.sprainkle.spring.cloud.advance.common.lock.api;

/**
 * <pre>
 *  不接受参数也不返回结果
 * </pre>
 *
 * @author sprainkle
 * @date 2018/1/27
 * @since 1.0.0
 */
@FunctionalInterface
public interface Action {
    /**
     *
     */
    void execute();
}
