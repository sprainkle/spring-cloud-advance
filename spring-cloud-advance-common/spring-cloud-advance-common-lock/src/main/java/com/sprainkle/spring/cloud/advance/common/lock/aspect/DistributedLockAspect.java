package com.sprainkle.spring.cloud.advance.common.lock.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.sprainkle.spring.cloud.advance.common.core.constant.enums.CommonResponseEnum;
import com.sprainkle.spring.cloud.advance.common.core.util.PointCutUtils;
import com.sprainkle.spring.cloud.advance.common.lock.annotation.DistributedLock;
import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockCallback;
import com.sprainkle.spring.cloud.advance.common.lock.api.DistributedLockTemplate;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.Ordered;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面逻辑. 其中对 SpEL 的支持, 参考: https://cloud.tencent.com/developer/article/1497676
 *
 * @author sprainkle
 * @date 2019/04/20
 */
@Slf4j
@Aspect
public class DistributedLockAspect implements ApplicationContextAware, BeanFactoryAware, Ordered {

    /**
     * 解析模板
     */
    private static final ParserContext PARSER_CONTEXT = ParserContext.TEMPLATE_EXPRESSION;
    /**
     * SpEL 解析器
     */
    private static final SpelExpressionParser spElParser = new SpelExpressionParser();

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;
    /**
     * 用于解析 @BeanName 为对应的 Spring Bean
     */
    private BeanResolver beanResolver;

    private final DistributedLockTemplate lockTemplate;

    public DistributedLockAspect(DistributedLockTemplate lockTemplate) {
        this.lockTemplate = lockTemplate;
    }


    @Around(value = "@annotation(distributedLock)")
    public Object doAround(ProceedingJoinPoint pjp, DistributedLock distributedLock) {
        EvaluationContext evaluationCtx = getEvaluationContext(pjp);

        doCheckBefore(distributedLock, evaluationCtx);

        String lockName = getLockName(pjp, evaluationCtx);

        return lock(pjp, lockName);
    }

    /**
     * 执行 {@link DistributedLock#checkBefore()} 指定的方法
     *
     * @param distributedLock
     * @param evaluationCtx
     */
    private void doCheckBefore(DistributedLock distributedLock, EvaluationContext evaluationCtx) {
        String checkBefore = distributedLock.checkBefore();
        resolveExpression(evaluationCtx, checkBefore);
    }

    /**
     * 获取锁名
     *
     * @param jp
     * @return
     */
    private String getLockName(JoinPoint jp, EvaluationContext evaluationCtx) {
        DistributedLock annotation = getAnnotation(jp);
        String lockName = annotation.lockName();

        if (StrUtil.isNotBlank(lockName)) {
            lockName = resolveExpression(evaluationCtx, lockName);
        } else {
            Object[] args = jp.getArgs();
            if (args.length > 0) {
                String param = annotation.param();
                if (StrUtil.isNotBlank(param)) {
                    Object arg;
                    if (annotation.argSeq() > 0) {
                        arg = args[annotation.argSeq() - 1];
                    } else {
                        arg = args[0];
                    }
                    lockName = String.valueOf(getParam(arg, param));
                } else if (annotation.argSeq() > 0) {
                    lockName = String.valueOf(args[annotation.argSeq() - 1]);
                }
            }
        }

        if (StrUtil.isBlank(lockName)) {
            CommonResponseEnum.SERVER_ERROR.assertFailWithMsg("无法生成分布式锁锁名. annotation: {0}", annotation);
        }

        String preLockName = annotation.lockNamePre();
        String postLockName = annotation.lockNamePost();
        String separator = annotation.separator();

        if (StrUtil.isNotBlank(preLockName)) {
            lockName = preLockName + separator + lockName;
        }
        if (StrUtil.isNotBlank(postLockName)) {
            lockName = lockName + separator + postLockName;
        }

        return lockName;
    }

    /**
     * 从方法参数获取数据
     *
     * @param param
     * @param arg 方法的参数数组
     * @return
     */
    private Object getParam(Object arg, String param) {

        if (StrUtil.isNotBlank(param) && arg != null) {
            try {
                return BeanUtil.getFieldValue(arg, param);
            } catch (Exception e) {
                CommonResponseEnum.SERVER_ERROR.assertFailWithMsg("[{0}] 没有属性 [{1}]", arg.getClass(), param);
            }
        }
        return "";
    }

    /**
     * 获取锁并执行
     *
     * @param pjp
     * @param lockName
     * @return
     */
    private Object lock(ProceedingJoinPoint pjp, final String lockName) {
        DistributedLock annotation = PointCutUtils.getAnnotation(pjp, DistributedLock.class);

        boolean fairLock = annotation.fairLock();
        boolean tryLock = annotation.tryLock();

        if (tryLock) {
            return tryLock(pjp, annotation, lockName, fairLock);
        } else {
            return lock(pjp,lockName, fairLock);
        }
    }

    /**
     *
     * @param pjp
     * @param lockName
     * @param fairLock
     * @return
     */
    private Object lock(ProceedingJoinPoint pjp, final String lockName, boolean fairLock) {
        return lockTemplate.lock(new DistributedLockCallback<Object>() {
            @Override
            public Object process() throws Throwable {
                return pjp.proceed();
            }

            @Override
            public String getLockName() {
                return lockName;
            }
        }, fairLock);
    }

    /**
     *
     * @param pjp
     * @param annotation
     * @param lockName
     * @param fairLock
     * @return
     */
    private Object tryLock(ProceedingJoinPoint pjp, DistributedLock annotation, final String lockName, boolean fairLock) {

        long waitTime = annotation.waitTime(), leaseTime = annotation.leaseTime();
        TimeUnit timeUnit = annotation.timeUnit();

        return lockTemplate.tryLock(new DistributedLockCallback<Object>() {
            @Override
            public Object process() throws Throwable {
                return pjp.proceed();
            }

            @Override
            public String getLockName() {
                return lockName;
            }
        }, waitTime, leaseTime, timeUnit, fairLock);
    }

    private DistributedLock getAnnotation(JoinPoint joinPoint) {
        return PointCutUtils.getAnnotation(joinPoint, DistributedLock.class);
    }

    private String resolveExpression(EvaluationContext ctx, String spElValue) {
        if (ctx == null || StrUtil.isBlank(spElValue)) {
            return spElValue;
        }

        spElValue = resolvePlaceholder(spElValue);
        if (!isSpEl(spElValue)) {
            return spElValue;
        }

        Object value = spElParser.parseExpression(spElValue, PARSER_CONTEXT).getValue(ctx);
        if (value == null) {
            return null;
        }

        return value.toString();
    }

    private boolean isSpEl(String spElValue) {
        if (StrUtil.isBlank(spElValue)) {
            return false;
        }

        return StrUtil.startWith(spElValue, "#{");
    }

    private String resolvePlaceholder(String value) {
        if (beanFactory != null && beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) beanFactory).resolveEmbeddedValue(value);
        }
        return value;
    }

    private EvaluationContext getEvaluationContext(JoinPoint jp) {
        if (!containSpEL(jp)) {
            return null;
        }

        // 获取方法的参数名和参数值
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        List<String> paramNames = Arrays.asList(methodSignature.getParameterNames());
        List<Object> params = Arrays.asList(jp.getArgs());

        Method method = PointCutUtils.getMethod(jp);
        Object[] args = PointCutUtils.getArgs(jp);
        Object target = jp.getThis();
        Class<?> targetClass = getTargetClass(target);
        DistributedLockExpressionRootObject rootObject = new DistributedLockExpressionRootObject(method, args, target, targetClass);

        // 将方法的参数名和参数值放入上下文中
        StandardEvaluationContext ctx = new StandardEvaluationContext(rootObject);
        ctx.setBeanResolver(beanResolver);
        for (int i = 0; i < paramNames.size(); i++) {
            ctx.setVariable(paramNames.get(i), params.get(i));
        }

        return ctx;
    }

    /**
     * 判断 {@link DistributedLock} 各支持 SpEL 的属性是否包含 SpEL
     *
     * @param jp
     * @return
     */
    private boolean containSpEL(JoinPoint jp) {
        DistributedLock annotation = getAnnotation(jp);

        String lockName = annotation.lockName();
        String checkBefore = annotation.checkBefore();

        if (isSpEl(lockName)) {
            return true;
        }

        if (isSpEl(checkBefore)) {
            return true;
        }

        return false;
    }

    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.beanResolver = new BeanFactoryResolver(beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 保证该切面逻辑是在 {@link org.springframework.transaction.annotation.Transactional} 切面之前被执行, 即在事务开始前执行
     *
     * @return
     */
    @Override
    public int getOrder() {
        try {
            int minOrder = Integer.MAX_VALUE;

            Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableTransactionManagement.class);
            for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
                Object value = entry.getValue();

                Class<?> proxyTargetClass = ClassUtils.getUserClass(value);
                EnableTransactionManagement annotation = proxyTargetClass.getAnnotation(EnableTransactionManagement.class);
                int order = annotation.order();
                minOrder = Math.min(order, minOrder);
            }

            return Math.min(0, minOrder) - 1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
