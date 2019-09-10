package com.sprainkle.spring.cloud.advance.common.core.operator;

import cn.hutool.core.collection.CollUtil;
import com.sprainkle.spring.cloud.advance.common.core.constant.enums.CommonResponseEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * <pre>
 *  {@link Operator} 路由器
 * </pre>
 *
 * @author sprainkle
 * @date 2019-09-09
 */
@Slf4j
public abstract class OperatorRouter<K, O extends Operator> {

    /**
     * 存放同一类型的{@link Operator}
     */
    private Map<K, O> operatorMap = Collections.emptyMap();

    /**
     * 根据 route key 路由到目标{@link Operator}
     * @param routeKey
     * @return
     */
    public O route(K routeKey) {
        O o = operatorMap.get(routeKey);
        if (o == null) {
            handleBadRoute(routeKey);
        }
        return o;
    }

    /**
     * 处理路由结果为空的情况. {@link #route(Object)}
     * @param routeKey
     */
    protected void handleBadRoute(K routeKey) {
        log.error("{} 无法路由到目标 Operator. routeKey: {}", this.getClass().getName(), routeKey);
        CommonResponseEnum.SERVER_ERROR.assertFail();
    }

    /**
     * 返回{@link Operator}的子类的{@link Class}
     * @return {@link O#getClass()}
     */
    protected abstract Class<O> getOperatorClass();

    /**
     * 在初始化{@link OperatorRouter}时, 会对所有 {@link Operator}进行校验, 确保初始化完成后的{@link OperatorRouter},
     * 其管理的 {@link Operator} 都是可用的, 校验逻辑默认为直接放行, 当子类需要对其管理的 {@link Operator} 进行校验时, 可重写该方法.
     *
     * @param operator
     */
    protected void checkOperator(O operator) {}

    void setOperatorMap(Map<K, O> operatorMap) {
        this.operatorMap = CollUtil.isEmpty(operatorMap) ? Collections.emptyMap() : operatorMap;
    }

}
