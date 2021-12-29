package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeMonthlyPlanDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.app.service.HmeMonthlyPlanService;
import com.ruike.hme.domain.vo.HmeMonthlyPlanVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName HmeMonthlyPlanController
 * @Description 月度计划报表
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/9 11:38
 * @Version 1.0
 **/

@RestController("hmeMonthlyPlanController.v1")
@RequestMapping("/v1/{organizationId}/hme-monthly-plan")
@Api(tags = SwaggerApiConfig.HME_DEMO)
public class HmeMonthlyPlanController {

    @Autowired
    private HmeMonthlyPlanService hmeMonthlyPlanService;

    @ApiOperation(value = "月度计划报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/old")
    public ResponseEntity<Page<HmeMonthlyPlanVO>> monthlyPlanQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                   HmeMonthlyPlanDTO dto,
                                                                   PageRequest pageRequest) {
        Page<HmeMonthlyPlanVO> hmeMonthlyPlanVO = hmeMonthlyPlanService.monthlyPlanQuery(tenantId, dto, pageRequest);
        return Results.success(hmeMonthlyPlanVO);
    }

    @ApiOperation(value = "月度计划报表-新版")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeMonthlyPlanVO>> monthlyPlanQueryNew(@PathVariable(value = "organizationId") Long tenantId,
                                                                   HmeMonthlyPlanDTO dto,
                                                                   PageRequest pageRequest) {
        Page<HmeMonthlyPlanVO> hmeMonthlyPlanVO = hmeMonthlyPlanService.monthlyPlanQueryNew(tenantId, dto, pageRequest);
        return Results.success(hmeMonthlyPlanVO);
    }

    @ApiOperation(value = "月度计划报表-导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export/old")
    @ExcelExport(HmeMonthlyPlanVO.class)
    public ResponseEntity<List<HmeMonthlyPlanVO>> monthlyPlanExport(@PathVariable(value = "organizationId") Long tenantId,
                                                                    HmeMonthlyPlanDTO dto,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response) {
        return Results.success(hmeMonthlyPlanService.monthlyPlanExport(tenantId, dto));
    }

    @ApiOperation(value = "月度计划报表-新版导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeMonthlyPlanVO.class)
    public ResponseEntity<List<HmeMonthlyPlanVO>> monthlyPlanExportNew(@PathVariable(value = "organizationId") Long tenantId,
                                                                    HmeMonthlyPlanDTO dto,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response) {
        return Results.success(hmeMonthlyPlanService.monthlyPlanExportNew(tenantId, dto));
    }
}
