package com.sprainkle.spring.cloud.advance.common.core.constant;

/**
 * <pre>
 *  异常返回码枚举接口
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public interface IResponseEnum {
    /**
     * 获取返回码
     * @return 返回码
     */
    int getCode();

    /**
     * 获取返回信息
     * @return 返回信息
     */
    String getMessage();
}
