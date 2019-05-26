package com.sprainkle.ueh.licence.client;

import com.sprainkle.spring.cloud.advance.common.core.pojo.response.R;
import com.sprainkle.spring.cloud.advance.proto.organization.response.OrganizationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/4
 */
@FeignClient("ueh-organization")
public interface OrganizationClient {

    @GetMapping("/organization/{organizationId}")
    R<OrganizationDTO> getOrganization(@PathVariable("organizationId") Long organizationId);

}
