package com.sprainkle.spring.cloud.advance.common.core.operator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;

/**
 * <pre>
 *  {@link Operator} 自动配置类
 * </pre>
 *
 * @author sprainkle
 * @date 2019-09-09
 */
@Configuration
public class OperatorAutoConfiguration {

    @Autowired(required = false)
    public void initOperatorRouter(Map<String, OperatorRouter> routerMap, ApplicationContext applicationContext) {
        if (CollUtil.isNotEmpty(routerMap)) {

            routerMap.values().forEach(router -> {
                Class<Operator> operatorClass = router.getOperatorClass();
                Map<String, Operator> beans = applicationContext.getBeansOfType(operatorClass);

                Map<Object, Operator> tmpMap = MapUtil.newHashMap(beans.size());

                beans.forEach((beanName, operator) -> {
                    router.checkOperator(operator);
                    tmpMap.put(operator.getName(), operator);
                });

                router.setOperatorMap(Collections.unmodifiableMap(tmpMap));
            });

        }
    }

}
