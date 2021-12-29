package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.app.service.HmeEmployeeAttendanceService;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO12;
import com.ruike.hme.domain.vo.HmeEmployeeAttendanceExportVO5;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 员工产量报表
 *
 *@author penglin.sui@hand-china.com 2021-07-12 19:38:00
 */
@RestController("HmeEmployeeAttendanceController.v1")
@RequestMapping("/v1/{organizationId}/hme-employee-export")
@Api(tags = "hmeEmployeeAttendance")
public class HmeEmployeeAttendanceController extends BaseController {

    @Autowired
    private HmeEmployeeAttendanceService hmeEmployeeAttendanceService;

    @ApiOperation(value = "员工产量汇总报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/sum")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO5>> sumQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                         HmeEmployeeAttendanceDTO13 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceService.sumQueryNew(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "员工产量汇总报表-导出")
    @GetMapping(value = "/sum/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeEmployeeAttendanceExportVO5.class)
    public ResponseEntity<List<HmeEmployeeAttendanceExportVO5>> sumExport(@PathVariable("organizationId") Long tenantId,
                                                                          ExportParam exportParam,
                                                                          HttpServletResponse response,
                                                                          HmeEmployeeAttendanceDTO13 dto) {
        return Results.success(hmeEmployeeAttendanceService.sumExportNew(tenantId, dto));
    }

    @ApiOperation(value = "员工产量汇总报表异步导出")
    @PostMapping("/async-export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> asyncExport(@PathVariable("organizationId") Long tenantId,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response,
                                                               @RequestBody HmeEmployeeAttendanceDTO13 dto) throws IOException {
        hmeEmployeeAttendanceService.asyncExport(tenantId, dto, request, response);
        return Results.success();
    }

    @ApiOperation(value = "员工产量汇总报表创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      String fileName){
        HmeExportTaskVO exportTaskVO = hmeEmployeeAttendanceService.createTask(tenantId, request, response, fileName);
        return Results.success(exportTaskVO);
    }


    @ApiOperation(value = "工段产量报表-头表查询逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/findOne")
    public ResponseEntity<Page<HmeEmployeeAttendanceDto>> findOneList(@PathVariable(value = "organizationId") Long tenantId,
                                                                      HmeEmployeeAttendanceDto1 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceService.headDataQuery(tenantId,dto,pageRequest));
    }
    @ApiOperation(value = "工段产量报表-表行明细查询逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/findInfoList")
    public ResponseEntity<Page<HmeEmployeeAttendanceRecordDto>> findInfoList(@PathVariable(value = "organizationId") Long tenantId,
                                                                             HmeEmployeeAttendanceDto5 dto, PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceService.lineDataQuery(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "工段产量报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/line-workcell-product-export")
    @ExcelExport(HmeEmployeeAttendanceDto.class)
    public ResponseEntity<List<HmeEmployeeAttendanceDto>> lineWorkcellProductExport(@PathVariable(value = "organizationId") Long tenantId,
                                                                                    HmeEmployeeAttendanceDto1 dto,
                                                                                    ExportParam exportParam,
                                                                                    HttpServletResponse response) {
        return Results.success(hmeEmployeeAttendanceService.lineWorkcellProductExport(tenantId,dto));
    }

    @ApiOperation(value = "员工产量汇总报表明细")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/summarys-detail")
    public ResponseEntity<Page<HmeEmployeeAttendanceExportVO12>> querySummarysDetail(@PathVariable(value = "organizationId") Long tenantId,
                                                                                     HmeEmployeeAttendanceDTO17 dto,
                                                                                     PageRequest pageRequest) {
        return Results.success(hmeEmployeeAttendanceService.querySummarysDetail(tenantId,dto,pageRequest));
    }
}
