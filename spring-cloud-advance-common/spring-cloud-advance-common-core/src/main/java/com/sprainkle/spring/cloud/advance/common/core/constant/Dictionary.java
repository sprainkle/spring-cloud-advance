package com.sprainkle.spring.cloud.advance.common.core.constant;

/**
 * <pre>
 *  字典接口
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public interface Dictionary {
    /**
     *  字典代码
     * @return
     */
    String getCode();

    /**
     * 字典名称
     * @return
     */
    String getName();

    /**
     * 判断字典代码是否相同
     * @param code
     * @return
     */
    default boolean equalsCode(String code) {
        return getCode().equals(code);
    }
}