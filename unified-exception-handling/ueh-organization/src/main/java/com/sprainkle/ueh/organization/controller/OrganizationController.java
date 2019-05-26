package com.sprainkle.ueh.organization.controller;

import com.sprainkle.spring.cloud.advance.common.core.pojo.response.R;
import com.sprainkle.spring.cloud.advance.proto.organization.response.OrganizationDTO;
import com.sprainkle.ueh.organization.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/18
 */
@RequestMapping("/organization")
@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/{organizationId}")
    public R<OrganizationDTO> getOrganization(@PathVariable("organizationId") Long organizationId) {
        return new R<>(organizationService.queryDetail(organizationId));
    }

}
