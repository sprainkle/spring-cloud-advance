package com.sprainkle.spring.cloud.advance.proto.licence.param;

import com.sprainkle.spring.cloud.advance.common.core.pojo.param.QueryParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * <pre>
 *  分页查询参数
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LicenceParam extends QueryParam {

    @NotBlank(message = "licence type cannot be empty.")
    private String licenceType;

}
