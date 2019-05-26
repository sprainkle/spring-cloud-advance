package com.sprainkle.ueh.organization.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sprainkle.spring.cloud.advance.proto.organization.response.OrganizationDTO;
import com.sprainkle.ueh.organization.constant.ResponseEnum;
import com.sprainkle.ueh.organization.entity.Organization;
import com.sprainkle.ueh.organization.mapper.OrganizationMapper;
import org.springframework.stereotype.Service;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/18
 */
@Service
public class OrganizationService extends ServiceImpl<OrganizationMapper, Organization> {

    /**
     * get organization detail by id.
     * @param orgId organization id.
     * @return {@link OrganizationDTO}
     */
    public OrganizationDTO queryDetail(Long orgId) {
        Organization organization = this.getById(orgId);
        ResponseEnum.ORGANIZATION_NOT_FOUND.assertNotNull(organization);

        return toOrganizationDTO(organization);
    }

    /**
     * entity -> dto
     * @param organization {@link Organization}
     * @return
     */
    private OrganizationDTO toOrganizationDTO(Organization organization) {
        OrganizationDTO organizationDTO= new OrganizationDTO();
        organizationDTO.setId(organization.getId());
        organizationDTO.setName(organization.getName());
        organizationDTO.setContactName(organization.getContactName());
        organizationDTO.setContactEmail(organization.getContactEmail());
        organizationDTO.setContactPhone(organization.getContactPhone());
        return organizationDTO;
    }

}
