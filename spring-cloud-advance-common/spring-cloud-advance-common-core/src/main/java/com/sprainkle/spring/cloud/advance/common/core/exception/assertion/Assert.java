package com.sprainkle.spring.cloud.advance.common.core.exception.assertion;

import cn.hutool.core.util.ArrayUtil;
import com.sprainkle.spring.cloud.advance.common.core.exception.BaseException;
import com.sprainkle.spring.cloud.advance.common.core.exception.WrapMessageException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>枚举类异常断言，提供简便的方式判断条件，并在条件满足时抛出异常</p>
 * <p>错误码和错误信息定义在枚举类中，在本断言方法中，传递错误信息需要的参数</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
public interface Assert {
    /**
     * 创建异常
     *
     * @param args
     * @return
     */
    BaseException newException(Object... args);

    /**
     * 创建异常
     *
     * @param t
     * @param args
     * @return
     */
    BaseException newException(Throwable t, Object... args);

    /**
     * 创建异常.
     * 先使用 {@code errMsg} 创建一个 {@link WrapMessageException} 异常,
     * 再以入参的形式传给 {{{@link #newException(Throwable, Object...)}}}, 作为最后创建的异常的 cause 属性.
     *
     * @param errMsg 自定义的错误信息
     * @param args
     * @return
     */
    default BaseException newExceptionWithMsg(String errMsg, Object... args) {
        if (args != null && args.length > 0) {
            errMsg = MessageFormat.format(errMsg, args);
        }

        WrapMessageException e = new WrapMessageException(errMsg);
        throw newException(e, args);
    }

    /**
     * 创建异常.
     * 先使用 {@code errMsg} 和 {@code t} 创建一个 {@link WrapMessageException} 异常,
     * 再以入参的形式传给 {{{@link #newException(Throwable, Object...)}}}, 作为最后创建的异常的 cause 属性.
     *
     * @param errMsg 自定义的错误信息
     * @param args
     * @return
     */
    default BaseException newExceptionWithMsg(String errMsg, Throwable t, Object... args) {
        if (ArrayUtil.isNotEmpty(args)) {
            errMsg = MessageFormat.format(errMsg, args);
        }

        WrapMessageException e = new WrapMessageException(errMsg, t);
        throw newException(e, args);
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj 待判断对象
     */
    default void assertNotNull(Object obj) {
        if (obj == null) {
            throw newException();
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj 待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertNotNullWithMsg(Object obj, String errMsg) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotNullWithMsg(Object obj, String errMsg, Object... args) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertNotNullWithMsg(Object obj, Supplier<String> errMsg) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotNullWithMsg(Object obj, Supplier<String> errMsg, Object... args) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * @param str 待判断字符串
     */
    default void assertNotEmpty(String str) {
        if (null == str || "".equals(str.trim())) {
            throw newException();
        }
    }

    /**
     * <p>断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param str 待判断字符串
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(String str, Object... args) {
        if (str == null || "".equals(str.trim())) {
            throw newException(args);
        }
    }

    /**
     * <p>断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * @param str 待判断字符串
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(String str, String errMsg) {
        if (null == str || "".equals(str.trim())) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param str 待判断字符串
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(String str, String errMsg, Object... args) {
        if (str == null || "".equals(str.trim())) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * @param arrays 待判断数组
     */
    default void assertNotEmpty(Object[] arrays) {
        if (arrays == null || arrays.length == 0) {
            throw newException();
        }
    }

    /**
     * <p>断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param arrays 待判断数组
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Object[] arrays, Object... args) {
        if (arrays == null || arrays.length == 0) {
            throw newException(args);
        }
    }

    /**
     * <p>断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Object[] arrays, String errMsg) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Object[] arrays, String errMsg, Object... args) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Object[] arrays, Supplier<String> errMsg) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Object[] arrays, Supplier<String> errMsg, Object... args) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     */
    default void assertNotEmpty(Collection<?> c) {
        if (c ==  null || c.isEmpty()) {
            throw newException();
        }
    }

    /**
     * <p>断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Collection<?> c, Object... args) {
        if (c ==  null || c.isEmpty()) {
            throw newException(args);
        }
    }

    /**
     * <p>断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Collection<?> c, String errMsg) {
        if (c ==  null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Collection<?> c, String errMsg, Object... args) {
        if (c ==  null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Collection<?> c, Supplier<String> errMsg) {
        if (c ==  null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Collection<?> c, Supplier<String> errMsg, Object... args) {
        if (c ==  null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     */
    default void assertNotEmpty(Map<?, ?> map) {
        if (map ==  null || map.isEmpty()) {
            throw newException();
        }
    }

    /**
     * <p>断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Map<?, ?> map, Object... args) {
        if (map ==  null || map.isEmpty()) {
            throw newException(args);
        }
    }

    /**
     * <p>断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, String errMsg) {
        if (map ==  null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, String errMsg, Object... args) {
        if (map ==  null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, Supplier<String> errMsg) {
        if (map ==  null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, Supplier<String> errMsg, Object... args) {
        if (map ==  null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     */
    default void assertIsFalse(boolean expression) {
        if (expression) {
            throw newException();
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalse(boolean expression, Object... args) {
        if (expression) {
            throw newException(args);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsFalseWithMsg(boolean expression, String errMsg) {
        if (expression) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalseWithMsg(boolean expression, String errMsg, Object... args) {
        if (expression) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsFalseWithMsg(boolean expression, Supplier<String> errMsg) {
        if (expression) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalseWithMsg(boolean expression, Supplier<String> errMsg, Object... args) {
        if (expression) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     */
    default void assertIsTrue(boolean expression) {
        if (!expression) {
            throw newException();
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrue(boolean expression, Object... args) {
        if (!expression) {
            throw newException(args);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsTrueWithMsg(boolean expression, String errMsg) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrueWithMsg(boolean expression, String errMsg, Object... args) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsTrueWithMsg(boolean expression, Supplier<String> errMsg) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrueWithMsg(boolean expression, Supplier<String> errMsg, Object... args) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断对象
     */
    default void assertIsNull(Object obj) {
        if (obj != null) {
            throw newException();
        }
    }

    /**
     * <p>断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsNull(Object obj, Object... args) {
        if (obj != null) {
            throw newException(args);
        }
    }

    /**
     * <p>断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertIsNullWithMsg(Object obj, String errMsg) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsNullWithMsg(Object obj, String errMsg, Object... args) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertIsNullWithMsg(Object obj, Supplier<String> errMsg) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsNullWithMsg(Object obj, Supplier<String> errMsg, Object... args) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     */
    default void assertEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newException();
        }
    }

    /**
     * <p>断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertEquals(Object o1, Object o2, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newException(args);
        }
    }

    /**
     * <p>断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertEqualsWithMsg(Object o1, Object o2, String errMsg) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * <p>断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertEqualsWithMsg(Object o1, Object o2, String errMsg, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * <p>断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertEqualsWithMsg(Object o1, Object o2, Supplier<String> errMsg) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * <p>断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。
     * 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2  待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertEqualsWithMsg(Object o1, Object o2, Supplier<String> errMsg, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * <p>直接抛出异常
     *
     */
    default void assertFail() {
        throw newException();
    }

    /**
     * <p>直接抛出异常
     *
     * @param args message占位符对应的参数列表
     */
    default void assertFail(Object... args) {
        throw newException(args);
    }

    /**
     * <p>直接抛出异常，并包含原异常信息
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，
     * 必须传递原始异常，作为新异常的cause
     *
     * @param t 原始异常
     */
    default void assertFail(Throwable t) {
        throw newException(t);
    }

    /**
     * <p>直接抛出异常，并包含原异常信息
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，
     * 必须传递原始异常，作为新异常的cause
     *
     * @param t 原始异常
     * @param args message占位符对应的参数列表
     */
    default void assertFail(Throwable t, Object... args) {
        throw newException(t, args);
    }

    /**
     * <p>直接抛出异常
     *
     * @param errMsg 自定义的错误信息
     */
    default void assertFailWithMsg(String errMsg) {
        throw newExceptionWithMsg(errMsg);
    }

    /**
     * <p>直接抛出异常
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(String errMsg, Object... args) {
        throw newExceptionWithMsg(errMsg, args);
    }

    /**
     * <p>直接抛出异常，并包含原异常信息
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，
     * 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息
     * @param t 原始异常
     */
    default void assertFailWithMsg(String errMsg, Throwable t) {
        throw newExceptionWithMsg(errMsg, t);
    }

    /**
     * <p>直接抛出异常，并包含原异常信息
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，
     * 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param t 原始异常
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(String errMsg, Throwable t, Object... args) {
        throw newExceptionWithMsg(errMsg, t, args);
    }

    /**
     * <p>直接抛出异常
     *
     * @param errMsg 自定义的错误信息
     */
    default void assertFailWithMsg(Supplier<String> errMsg) {
        throw newExceptionWithMsg(errMsg.get());
    }

    /**
     * <p>直接抛出异常
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(Supplier<String> errMsg, Object... args) {
        throw newExceptionWithMsg(errMsg.get(), args);
    }

    /**
     * <p>直接抛出异常，并包含原异常信息
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，
     * 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息
     * @param t 原始异常
     */
    default void assertFailWithMsg(Supplier<String> errMsg, Throwable t) {
        throw newExceptionWithMsg(errMsg.get(), t);
    }

    /**
     * <p>直接抛出异常，并包含原异常信息
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时，
     * 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param t 原始异常
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(Supplier<String> errMsg, Throwable t, Object... args) {
        throw newExceptionWithMsg(errMsg.get(), t, args);
    }

}
