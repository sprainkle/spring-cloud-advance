package com.sprainkle.event.model;

import com.sprainkle.spring.cloud.advance.common.core.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  订单 消息模型
 * </pre>
 *
 * @author sprainkle
 * @date 2019/6/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {

    /**
     * 订单id
     */
    private Long id;

    /**
     * 订单失效时间
     */
    private Long expireTime;

    @Override
    public String toString() {
        return "OrderModel{" +
                "id=" + id +
                ", expireTime=" + TimeUtil.format(TimeUtil.toLocalDateTime(expireTime)) +
                '}';
    }
}
