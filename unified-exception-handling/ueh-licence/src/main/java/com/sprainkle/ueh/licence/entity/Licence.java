package com.sprainkle.ueh.licence.entity;


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
@TableName("licence")
public class Licence {

    @TableId(value = "licence_id", type = IdType.AUTO)
    private Long licenceId;

    private Long organizationId;

    private String licenceType;

    private String productName;

    private Integer licenceMax;

    private Integer licenceAllocated;

    private String comment;

}
