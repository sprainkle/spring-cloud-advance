package com.sprainkle.spring.cloud.advance.common.core.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Spring AOP point cut 工具类
 *
 * @author sprainkle
 * @date 2021/8/13
 */
public class PointCutUtils {

    public static MethodSignature getMethodSignature(JoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    public static Method getMethod(JoinPoint joinPoint) {
        return getMethodSignature(joinPoint).getMethod();
    }

    public static Object[] getArgs(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    public static <T extends Annotation> T getAnnotation(JoinPoint joinPoint, Class<T> annotationClass) {
        Method method = getMethod(joinPoint);
        return AnnotationUtils.getAnnotation(method, annotationClass);
    }

}
