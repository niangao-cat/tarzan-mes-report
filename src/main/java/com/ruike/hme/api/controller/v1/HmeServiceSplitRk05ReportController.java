package com.ruike.hme.api.controller.v1;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

import com.ruike.hme.api.dto.HmeServiceSplitRk05ReportDTO;
import com.ruike.hme.app.service.HmeServiceSplitRk05ReportService;
import com.ruike.hme.domain.vo.HmeServiceSplitRk05ReportVO;
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
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

/**
 * 售后在制品盘点-半成品报表
 *
 * @author penglin.sui@hand-china.com 2021/03/31 16:11
 */

@RestController("hmeServiceSplitRk05ReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-service-split-rk05-report")
@Api(tags = SwaggerApiConfig.HME_SERVICE_SPLIT_RK05_REPORT)
public class HmeServiceSplitRk05ReportController {
    @Autowired
    private HmeServiceSplitRk05ReportService hmeServiceSplitRk05ReportService;

    @ApiOperation(value = "售后在制品盘点-半成品报表查询")
    @PostMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeServiceSplitRk05ReportVO>> selectSplitRecordList(@PathVariable("organizationId") Long tenantId,
                                                                                   @RequestBody HmeServiceSplitRk05ReportDTO dto,
                                                                                   PageRequest pageRequest) {
        return Results.success(hmeServiceSplitRk05ReportService.selectSplitRecordList(tenantId, dto, pageRequest));
    }
    @ApiOperation(value = "售后在制品盘点-半成品报表导出EXCEL")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(HmeServiceSplitRk05ReportVO.class)
    public ResponseEntity<List<HmeServiceSplitRk05ReportVO>> serviceSplitRk05Export(@PathVariable("organizationId") Long tenantId,
                                                                                    HmeServiceSplitRk05ReportDTO dto,
                                                                                    HttpServletResponse response,
                                                                                    ExportParam exportParam) {
        return Results.success(hmeServiceSplitRk05ReportService.serviceSplitRk05Export(tenantId, dto));
    }
}
