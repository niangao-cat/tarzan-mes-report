package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeWoPlanRateReportDTO;
import com.ruike.hme.app.service.HmeWoPlanRateReportService;
import com.ruike.hme.domain.vo.HmeWoPlanRateReportVO;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import java.util.List;

/**
 * description 工单计划达成率报表
 *
 * @author wenzhang.yu@hand-china.com 2021/03/09 20:26
 */
@RestController("hmeWoPlanRateReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-wo-plan-rate-report")
@Api(tags = SwaggerApiConfig.HME_DEMO)
public class HmeWoPlanRateReportController extends BaseController {

    @Autowired
    private HmeWoPlanRateReportService hmeWoPlanRateReportService;

    @ApiOperation(value = "工单计划达成率报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query", produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<HmeWoPlanRateReportVO>> woPlanRateReportQuery(@PathVariable("organizationId") Long tenantId,
                                                                             HmeWoPlanRateReportDTO dto) {
        List<HmeWoPlanRateReportVO> hmeWoPlanRateReportVO = hmeWoPlanRateReportService.woPlanRateReportQuery(tenantId, dto);
        return Results.success(hmeWoPlanRateReportVO);
    }


}
