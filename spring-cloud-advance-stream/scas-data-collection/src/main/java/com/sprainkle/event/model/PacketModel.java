package com.sprainkle.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 *  数据包 模型
 * </pre>
 *
 * @author sprainkle
 * @date 2019/6/2
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacketModel {

    /**
     * 设备 eui
     */
    private String devEui;

    /**
     * 数据
     */
    private String data;

    // 省略其他字段

}
