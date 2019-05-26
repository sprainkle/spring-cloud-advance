package com.sprainkle.ueh.licence.controller;

import com.sprainkle.spring.cloud.advance.common.core.pojo.response.QR;
import com.sprainkle.spring.cloud.advance.common.core.pojo.response.R;
import com.sprainkle.spring.cloud.advance.proto.licence.param.LicenceParam;
import com.sprainkle.spring.cloud.advance.proto.licence.request.LicenceAddRequest;
import com.sprainkle.spring.cloud.advance.proto.licence.response.LicenceAddRespData;
import com.sprainkle.spring.cloud.advance.proto.licence.response.LicenceDTO;
import com.sprainkle.spring.cloud.advance.proto.licence.response.SimpleLicenceDTO;
import com.sprainkle.ueh.licence.service.LicenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *
 * </pre>
 *
 * @author sprainkle
 * @date 2019/5/3
 */
@RestController
@RequestMapping(value = "/licence")
public class LicenceController {

    @Autowired
    private LicenceService licenceService;

    @GetMapping(value = "/{licenceId}")
    public R<LicenceDTO> getLicence(@PathVariable("licenceId") Long licenceId) {
        return new R<>(licenceService.queryDetail(licenceId));
    }

    @GetMapping(value = "/list")
    public QR<SimpleLicenceDTO> getLicences(@Validated LicenceParam licenceParam) {
        return new QR<>(licenceService.getLicences(licenceParam));
    }

    @PostMapping
    public R<LicenceAddRespData> addLicence(@Validated @RequestBody LicenceAddRequest request) {
        return new R<>(licenceService.addLicence(request));
    }

}
