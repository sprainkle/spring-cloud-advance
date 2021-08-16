package com.sprainkle.spring.cloud.advance.common.lock.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author sprainkle
 * @date 2021/8/14
 */
@Data
@TableName("test_item")
public class TestItem {

    @TableId
    private Integer id;

    private String name;

    private Integer stock;

    public TestItem() {
    }

    public TestItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

}
