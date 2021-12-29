package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.app.service.HmeRepairProductPassRateService;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO8;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

/**
 * HmeRepairProductPassRateController
 *
 * @author: chaonan.hu@hand-china.com 2021-05-19 11:12:21
 **/
@RestController("hmeRepairProductPassRateController.v1")
@RequestMapping("/v1/{organizationId}/hme-repair-product-pass-rate")
public class HmeRepairProductPassRateController {

    @Autowired
    private HmeRepairProductPassRateService hmeRepairProductPassRateService;

    private static final String FILE_REPAIR_PRODUCT_PASS_RATE = "返修产品直通率报表";
    private static final String FILE_REPAIR_PRODUCT_DAY_PASS_RATE = "返修日直通率报表";

    @ApiOperation(value = "返修产品直通率报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<HmeProdLinePassRateVO5> repairProductPassRateQuery(@PathVariable(value = "organizationId")  Long tenantId,
                                                                             HmeProdLinePassRateDTO dto){
        return Results.success(hmeRepairProductPassRateService.repairProductPassRateQuery(tenantId, dto));
    }

    @ApiOperation(value = "返修产品直通率报表导出")
    @PostMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> onlineReportExport(@PathVariable("organizationId") Long tenantId,
                                                @RequestBody HmeProdLinePassRateDTO dto, HttpServletResponse response) {
        try {
            hmeRepairProductPassRateService.repairProductPassRateExport(tenantId, dto, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }

    @ApiOperation(value = "返修产品直通率报表异步导出")
    @PostMapping(value = "/async-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> repairProductPassRateAsyncExport(@PathVariable("organizationId") Long tenantId,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              @RequestBody HmeProdLinePassRateDTO dto) throws IOException {
        hmeRepairProductPassRateService.repairProductPassRateAsyncExport(tenantId, dto, request, response);
        return Results.success();
    }

    @ApiOperation(value = "返修产品日直通率报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/day")
    public ResponseEntity<HmeProdLinePassRateVO8> repairProductDayPassRateQuery(@PathVariable(value = "organizationId")  Long tenantId,
                                                                                HmeProdLinePassRateDTO2 dto) throws ParseException {
        return Results.success(hmeRepairProductPassRateService.repairProductDayPassRateQuery(tenantId, dto));
    }

    @ApiOperation(value = "返修日直通率报表导出")
    @PostMapping(value = "/day/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> repairProductDayPassRateExport(@PathVariable("organizationId") Long tenantId,
                                                            @RequestBody HmeProdLinePassRateDTO2 dto, HttpServletResponse response) {
        try {
            hmeRepairProductPassRateService.repairProductDayPassRateExport(tenantId, dto, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }

    @ApiOperation(value = "返修日直通率报表异步导出")
    @PostMapping(value = "/async-day-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> repairProductDayPassRateAsyncExport(@PathVariable("organizationId") Long tenantId,
                                                              HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              @RequestBody HmeProdLinePassRateDTO2 dto) throws IOException, ParseException {
        hmeRepairProductPassRateService.repairProductDayPassRateAsyncExport(tenantId, dto, request, response);
        return Results.success();
    }

    @ApiOperation(value = "返修产品直通率创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response){
        HmeExportTaskVO exportTaskVO = hmeRepairProductPassRateService.createTask(tenantId, request, response, FILE_REPAIR_PRODUCT_PASS_RATE);
        return Results.success(exportTaskVO);
    }

    @ApiOperation(value = "返修日直通率创建任务")
    @PostMapping("/create-day-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createDayTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response){
        HmeExportTaskVO exportTaskVO = hmeRepairProductPassRateService.createTask(tenantId, request, response, FILE_REPAIR_PRODUCT_DAY_PASS_RATE);
        return Results.success(exportTaskVO);
    }
}
