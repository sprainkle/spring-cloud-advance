package com.sprainkle.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sprainkle
 * @date 2020/3/24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DelayModel {

    /**
     * 延迟投递的时长. 单位: ms
     */
    private long delay;

}
