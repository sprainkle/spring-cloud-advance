package com.sprainkle.spring.cloud.advance.common.core.exception;

import com.sprainkle.spring.cloud.advance.common.core.constant.IResponseEnum;

/**
 * <p>参数异常</p>
 * <p>在处理业务过程中校验参数出现错误, 可以抛出该异常</p>
 * <p>编写公共代码（如工具类）时，对传入参数检查不通过时，可以抛出该异常</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public class ArgumentException extends  BaseException {

    private static final long serialVersionUID = 1L;

    public ArgumentException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public ArgumentException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
