package com.sprainkle.ueh.organization.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/3
 */
@Data
@TableName("organization")
public class Organization {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String contactName;

    private String contactEmail;

    private String contactPhone;
}
