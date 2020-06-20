package com.sprainkle.spring.cloud.advance.common.core.exception.assertion;

import cn.hutool.core.util.ArrayUtil;
import com.sprainkle.spring.cloud.advance.common.core.constant.IResponseEnum;
import com.sprainkle.spring.cloud.advance.common.core.exception.ArgumentException;
import com.sprainkle.spring.cloud.advance.common.core.exception.BaseException;

import java.text.MessageFormat;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public interface CommonExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new ArgumentException(this, args, msg, t);
    }

}
