package com.sprainkle.spring.cloud.advance.common.lock.enums;

import com.sprainkle.spring.cloud.advance.common.core.exception.assertion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回结果
 *
 * @author sprainkle
 * @date 2021/4/26
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum implements BusinessExceptionAssert {
    /**
     * 无法获得锁, 无法在指定时间内获得锁
     */
    LOCK_NOT_YET_HOLD(5100, "系统繁忙，请稍后重试"),
    /**
     * 获得锁后执行业务逻辑过程中, 锁异常释放, 即不再持有锁
     */
    LOCK_NO_MORE_HOLD(5101, "系统繁忙，请稍后重试"),
    ;


    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;
}
