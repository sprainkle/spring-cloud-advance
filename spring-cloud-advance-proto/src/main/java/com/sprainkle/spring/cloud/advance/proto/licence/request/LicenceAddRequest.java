package com.sprainkle.spring.cloud.advance.proto.licence.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/4
 */
@Data
public class LicenceAddRequest {

    @NotNull
    private Long organizationId;

    @NotBlank
    private String licenceType;

    private String productName;

    @Min(value = 0)
    private Integer licenceMax;

    @PositiveOrZero
    private Integer licenceAllocated;

    private String comment;
}
