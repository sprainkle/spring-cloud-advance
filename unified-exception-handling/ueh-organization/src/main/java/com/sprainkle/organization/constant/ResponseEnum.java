package com.sprainkle.organization.constant;

import com.sprainkle.spring.cloud.advance.common.core.exception.assertion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>返回结果</p>
 *
 * @author sprainkle
 * @date 2018.09.17
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum implements BusinessExceptionAssert {

    /**
     *
     */
    ORGANIZATION_NOT_FOUND(6001, "Organization not found.")

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
