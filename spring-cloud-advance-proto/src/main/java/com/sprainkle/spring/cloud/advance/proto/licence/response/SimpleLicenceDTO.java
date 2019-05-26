package com.sprainkle.spring.cloud.advance.proto.licence.response;

import lombok.Data;

/**
 * <pre>
 *  简单的Licence DTO, 缺少organization 的详细信息.
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/17
 */
@Data
public class SimpleLicenceDTO {
    private String licenceId;

    private String organizationId;

    private String licenceType;

    private String productName;

    private Integer licenceMax;

    private Integer licenceAllocated;

    private String comment;
}
