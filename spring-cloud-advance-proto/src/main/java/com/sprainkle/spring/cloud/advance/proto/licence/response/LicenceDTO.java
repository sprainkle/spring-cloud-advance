package com.sprainkle.spring.cloud.advance.proto.licence.response;

import lombok.Data;

/**
 * <pre>
 *  Licence DTO, 包含organization 的详细信息.
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/4
 */
@Data
public class LicenceDTO {

    private String licenceId;

    private String organizationId;

    private String organizationName;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private String licenceType;

    private String productName;

    private Integer licenceMax;

    private Integer licenceAllocated;

    private String comment;

}
