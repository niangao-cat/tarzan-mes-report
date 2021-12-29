package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO2;
import com.ruike.wms.api.dto.WmsDeliveryMonitoringDTO4;
import com.ruike.wms.app.service.WmsDeliveryMonitoringService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * @program: tarzan-mes-report
 * @name: WmsDeliveryMonitoringController
 * @description: 配送任务监控看板
 * @author: yuan.liu05@hand-china.com
 * @create: 2021-05-13 16:50
 **/
@RestController("wmsDeliveryMonitoringController.v1")
@RequestMapping("/v1/{organizationId}/delivery-monitoring-board")
@Api(tags = SwaggerApiConfig.WMS_DELIVERY_MONITORING)
@Slf4j
public class WmsDeliveryMonitoringController extends BaseController {

    @Autowired
    private WmsDeliveryMonitoringService wmsDeliveryMonitoringService;

    @ApiOperation(value = "日配送任务分布图")
    @GetMapping(value = {"/daily/data/query"}, produces = "application/json;charset=UTF-8")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<WmsDeliveryMonitoringDTO>> dailyDataQueryForUi(@PathVariable("organizationId") Long tenantId) {
        return Results.success(wmsDeliveryMonitoringService.selectDailyDelivery(tenantId));
    }

    @ApiOperation(value = "日产线配送任务进度表")
    @GetMapping(value = {"/proLine/data/query"}, produces = "application/json;charset=UTF-8")
    @Permission(permissionPublic = true)
    public ResponseEntity<Page<WmsDeliveryMonitoringDTO2>> proLineDataQueryForUi(@PathVariable("organizationId") Long tenantId, @ApiIgnore PageRequest pageRequest) {
        return Results.success(wmsDeliveryMonitoringService.selectProLineDelivery(tenantId, pageRequest));
    }

    @ApiOperation(value = "每月配送任务统计图")
    @GetMapping(value = {"/month/data/query"}, produces = "application/json;charset=UTF-8")
    @Permission(permissionPublic = true)
    public ResponseEntity<WmsDeliveryMonitoringDTO4> monthDataQueryForUi(@PathVariable("organizationId") Long tenantId) {
        return Results.success(wmsDeliveryMonitoringService.selectMonthDelivery(tenantId));
    }

}
