package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosFunctionDTO2;
import com.ruike.hme.api.dto.HmeCosFunctionHeadDTO;
import com.ruike.hme.app.service.HmeCosFunctionService;
import com.ruike.hme.domain.entity.HmeCosFunction;
import com.ruike.hme.domain.vo.HmeCosFunctionVO2;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
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
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 芯片性能表 管理 API
 *
 * @author wenzhang.yu@hand-china.com 2020-08-07 15:08:16
 */
@RestController("hmeCosFunctionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-functions")
@Slf4j
public class HmeCosFunctionController extends BaseController {

    @Autowired
    private HmeCosFunctionService hmeCosFunctionService;

    private static final String FILE_COS_FUNCTION = "COS测试明细报表";

    @ApiOperation(value = "COS测试明细报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/mes-report/function")
    public ResponseEntity<Page<HmeCosFunctionVO2>> cosFunctionReport(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody HmeCosFunctionDTO2 dto,
                                                                     PageRequest pageRequest) {
        log.info("<====HmeCosFunctionController-cosFunctionReport:{},{}", tenantId, dto);
        Page<HmeCosFunctionVO2> hmeCosFunctions = hmeCosFunctionService.cosFunctionReport(tenantId, dto, pageRequest);
        return Results.success(hmeCosFunctions);
    }

    @ApiOperation(value = "COS测试明细报表导出")
    @PostMapping(value = "/mes-report/function/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> exportDetail(@PathVariable("organizationId") Long tenantId,
                                          @RequestBody HmeCosFunctionDTO2 dto,
                                          HttpServletResponse response) throws IOException {
        hmeCosFunctionService.cosFunctionReportExport(tenantId, dto, response);
        return Results.success();
    }

    @ApiOperation(value = "COS测试明细报表异步导出")
    @PostMapping(value = "/mes-report/function/async-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> asyncExportDetail(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeCosFunctionDTO2 dto) throws IOException {
        hmeCosFunctionService.cosFunctionReportAsyncExport(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response){
        HmeExportTaskVO exportTaskVO = hmeCosFunctionService.createTask(tenantId, request, response, FILE_COS_FUNCTION);
        return Results.success(exportTaskVO);
    }

    @ApiOperation(value = "COS测试明细报表-GP")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/mes-report/gp/function")
    public ResponseEntity<Page<HmeCosFunctionVO2>> gpCosFunctionReport(@PathVariable("organizationId") Long tenantId,
                                                                       @RequestBody HmeCosFunctionDTO2 dto,
                                                                       PageRequest pageRequest) {
        log.info("<====HmeCosFunctionController-gpCosFunctionReport:{},{}", tenantId, dto);
//        Page<HmeCosFunctionVO2> hmeCosFunctions = hmeCosFunctionService.gpCosFunctionReport(tenantId, dto, pageRequest);
        Page<HmeCosFunctionVO2> hmeCosFunctions = hmeCosFunctionService.gpCosFunctionReport2(tenantId, dto, pageRequest);
        return Results.success(hmeCosFunctions);
    }

    @ApiOperation(value = "COS测试明细报表导出")
    @PostMapping(value = "/mes-report/gp/function/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> gpExportDetail(@PathVariable("organizationId") Long tenantId,
                                          @RequestBody HmeCosFunctionDTO2 dto,
                                          HttpServletResponse response) throws IOException {
//        hmeCosFunctionService.gpCosFunctionReportExport(tenantId, dto, response);
        hmeCosFunctionService.gpCosFunctionReportExport2(tenantId, dto, response);
        return Results.success();
    }

    @ApiOperation(value = "COS测试明细报表异步导出")
    @PostMapping(value = "/mes-report/gp/function/async-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> gpAsyncExportDetail(@PathVariable("organizationId") Long tenantId,
                                               @RequestBody HmeCosFunctionDTO2 dto) throws IOException {
//        hmeCosFunctionService.gpCosFunctionReportAsyncExport(tenantId, dto);
        hmeCosFunctionService.gpCosFunctionReportAsyncExport2(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "查询芯片性能头数据")
    @GetMapping(value = "/cosfunction/headquery", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeCosFunctionHeadDTO>> cosFunctionHeadQuery(@PathVariable("organizationId") Long tenantId,
                                                                            HmeCosFunctionHeadDTO dto,
                                                                            PageRequest pageRequest) {
        log.info("<====HmeCosFunctionController-cosFunctionHeadQuery:{},{}", tenantId, dto.getMaterialCode());
        Page<HmeCosFunctionHeadDTO> hmeCosContainers = hmeCosFunctionService.cosFunctionHeadQuery(tenantId, dto, pageRequest);
        return Results.success(hmeCosContainers);
    }

    @ApiOperation(value = "查询芯片性能数据")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/cosFunction/query")
    public ResponseEntity<Page<HmeCosFunction>> cosFunctionQuery(@PathVariable("organizationId") Long tenantId,
                                                                 String loadSequence,
                                                                 PageRequest pageRequest) {
        log.info("<====HmeCosFunctionController-cosFunctionQuery:{},{}", tenantId, loadSequence);
        Page<HmeCosFunction> hmeCosFunctions = hmeCosFunctionService.cosFunctionQuery(tenantId, loadSequence, pageRequest);
        return Results.success(hmeCosFunctions);
    }
}
