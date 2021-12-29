package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsDistributionGapDTO;
import com.ruike.wms.api.dto.WmsDistributionGapDTO3;
import com.ruike.wms.app.service.WmsDistributionGapService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

/**
 * @program: tarzan-mes-report
 * @name: WmsDistributionGapController
 * @description: 物料配送缺口看板
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-19 10:53
 **/
@RestController("wmsDistributionGapController.v1")
@RequestMapping("/v1/{organizationId}/distribution-gap-board")
@Api(tags = SwaggerApiConfig.WMS_DISTRIBUTION_GAP)
@Slf4j
public class WmsDistributionGapController {

    @Autowired
    private WmsDistributionGapService wmsDistributionGapService;

    @ApiOperation(value = "物料配送缺口")
    @GetMapping(value = {"/data/query"}, produces = "application/json;charset=UTF-8")
    @Permission(permissionPublic = true)
    public ResponseEntity<Page<WmsDistributionGapDTO>> proLineDataQueryForUi(@PathVariable("organizationId") Long tenantId,
                                                                             WmsDistributionGapDTO3 dto3,
                                                                             @ApiIgnore PageRequest pageRequest) {
        dto3.initParam();
        return Results.success(wmsDistributionGapService.selectDelivery(tenantId, dto3, pageRequest));
    }
}
