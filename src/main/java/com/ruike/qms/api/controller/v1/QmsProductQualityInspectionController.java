package com.ruike.qms.api.controller.v1;

import com.ruike.qms.app.service.QmsProductQualityInspectionService;
import com.ruike.qms.domain.vo.QmsProductQualityInspectionNcRecordVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 成品质量检验看板
 *
 * @author faming.yang@hand-china.com 2021-05-13 20:11
 */
@RestController("QmsProductQualityInspectionController.v1")
@RequestMapping("/v1/{organizationId}/product-quality-inspection")
public class QmsProductQualityInspectionController {
    @Autowired
    private QmsProductQualityInspectionService qmsProductQualityInspectionService;

    @ApiOperation(value = "成品质量检验看板")
    @Permission(permissionPublic = true)
    @GetMapping
    public ResponseEntity<?> qualityInspection(@PathVariable("organizationId") Long tenantId) {

        return Results.success(qmsProductQualityInspectionService.qualityInspection(tenantId));
    }


    @ApiOperation(value = "不良情况说明")
    @Permission(permissionPublic = true)
    @GetMapping("/nc-record")
    public ResponseEntity<List<QmsProductQualityInspectionNcRecordVO>> ncRecord(@PathVariable("organizationId") Long tenantId) {

        return Results.success(qmsProductQualityInspectionService.ncRecord(tenantId));
    }
}
