package com.sprainkle.spring.cloud.advance.common.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     可以使用该注解实现分布式锁。
 *
 *     获取lockName的优先级为：lockName > argNum > param
 *
 *     使用的是公平锁, 即先来先得.
 * </pre>
 *
 * @author sprainkle
 * @date 2019.04.20
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    /**
     * 锁的名称。
     * 如果lockName可以确定，直接设置该属性。
     * <br><br>
     * 支持 SpEL, 格式为: #{expression}, 内置 #root, 属性包括: target, method, args 等, 其中 target 为注解所在类的 Spring Bean
     * 也支持 占位符 ${}
     */
    String lockName() default "";

    /**
     * lockName 前缀
     */
    String lockNamePre() default "";

    /**
     * lockName 后缀
     * @see #LOCK
     */
    String lockNamePost() default "";

    /**
     * 在开始加锁前, 执行某个方法进行校验.
     * <br><br>
     * 支持 SpEL, 格式为: #{expression}, 所以方法必须为 public, 如果方法的所在 Spring Bean 与注解的方法相同,
     * 写法为: #{#root.target.yourMethod(#param1, #param2, ...)}
     *
     * @return
     */
    String checkBefore() default "";

    /**
     * 获得锁名时拼接前后缀用到的分隔符
     * @see #DEFAULT_SEPARATOR
     */
    String separator() default DEFAULT_SEPARATOR;

    /**
     * <pre>
     *     获取注解的方法参数列表的某个参数对象的某个属性值来作为lockName。因为有时候lockName是不固定的。
     *     当param不为空时，可以通过argSeq参数来设置具体是参数列表的第几个参数，不设置则默认取第一个。
     * </pre>
     */
    String param() default "";

    /**
     * 将方法第argSeq个参数作为锁名. 0为无效值.
     */
    int argSeq() default 0;

    /**
     * 是否使用公平锁。
     * 公平锁即先来先得。
     */
    boolean fairLock() default false;

    /**
     * 是否使用尝试锁。
     */
    boolean tryLock() default true;

    /**
     * 最长等待时间。
     * 该字段只有当tryLock()返回true才有效。
     * @see #DEFAULT_WAIT_TIME
     */
    long waitTime() default DEFAULT_WAIT_TIME;

    /**
     * <pre>
     *     锁超时时间。
     *     超时时间过后，锁自动释放。
     *     建议：
     *       尽量缩简需要加锁的逻辑。
     * </pre>
     * @see #DEFAULT_TIMEOUT
     */
    long leaseTime() default DEFAULT_TIMEOUT;

    /**
     * 时间单位。默认为毫秒。
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    /**
     * 尝试获取锁的默认等待时间
     */
    long DEFAULT_WAIT_TIME = 10000L;
    /**
     * 锁的默认超时时间. 超时后, 锁会被自动释放
     */
    long DEFAULT_TIMEOUT = 5000L;
    /**
     * 时间单位。默认为毫秒。
     */
    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;
    /**
     * 获得锁名时拼接前后缀用到的分隔符
     */
    String DEFAULT_SEPARATOR = ":";
    /**
     * lock
     */
    String LOCK = "lock";
}
