package com.sprainkle.spring.cloud.advance.common.lock.api.impl;


import com.sprainkle.spring.cloud.advance.common.core.constant.enums.CommonResponseEnum;
import com.sprainkle.spring.cloud.advance.common.core.exception.BaseException;
import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockCallback;
import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockTemplate;

/**
 * Base DistributedLockTemplate. 用于封装一些公共方法
 *
 * @author sprainkle
 * @date 2021/8/13
 */
public abstract class AbstractDistributedLockTemplate implements DistributedLockTemplate {

    /**
     * 处理业务逻辑
     *
     * @param callback
     * @param <T>
     * @return 业务逻辑处理结果
     */
    protected <T> T process(DistributedLockCallback<T> callback) {
        try {
            return callback.process();
        } catch (Throwable e) {
            if (e instanceof BaseException) {
                throw (BaseException) e;
            }

            throw new BaseException(CommonResponseEnum.SERVER_ERROR, null, e.getMessage(), e);
        }
    }

}
