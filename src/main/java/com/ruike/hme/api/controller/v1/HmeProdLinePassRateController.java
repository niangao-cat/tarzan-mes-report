package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO2;
import com.ruike.hme.app.service.HmeProdLinePassRateService;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO8;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * 产品直通率报表 管理API
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:22:12
 **/

@RestController("hmeProdLinePassRateController.v1")
@RequestMapping("/v1/{organizationId}/hme-prod-line-pass-rate")
public class HmeProdLinePassRateController {

    @Autowired
    private HmeProdLinePassRateService hmeProdLinePassRateService;

    private static final String FILE_PRODUCT_PASS_RATE = "产品直通率报表";
    private static final String FILE_PRODUCT_DAY_PASS_RATE = "日直通率报表";

    @ApiOperation(value = "产品直通率报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<HmeProdLinePassRateVO5> prodLinePassRateQuery(@PathVariable(value = "organizationId")  Long tenantId,
                                                                        HmeProdLinePassRateDTO dto){
        return Results.success(hmeProdLinePassRateService.prodLinePassRateQuery(tenantId, dto));
    }

    @ApiOperation(value = "产品直通率报表导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> onlineReportExport(@PathVariable("organizationId") Long tenantId,
                                                HmeProdLinePassRateDTO params, HttpServletResponse response) {
        try {
            hmeProdLinePassRateService.prodLinePassRateExport(tenantId, params, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }

    @ApiOperation(value = "产品直通率报表导出")
    @PostMapping("/async-export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> asyncOnlineReportExport(@PathVariable("organizationId") Long tenantId,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        @RequestBody HmeProdLinePassRateDTO dto) throws IOException {
        hmeProdLinePassRateService.asyncOnlineReportExport(tenantId, dto, request, response);
        return Results.success();
    }

    @ApiOperation(value = "产品日直通率报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/day")
    public ResponseEntity<HmeProdLinePassRateVO8> prodLineDayPassRateQuery(@PathVariable(value = "organizationId")  Long tenantId,
                                                                           HmeProdLinePassRateDTO2 dto) throws ParseException {
        return Results.success(hmeProdLinePassRateService.prodLineDayPassRateQuery(tenantId, dto));
    }

    @ApiOperation(value = "日直通率报表导出")
    @GetMapping(value = "/day/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> prodLineDayPassRateExport(@PathVariable("organizationId") Long tenantId,
                                                       HmeProdLinePassRateDTO2 params, HttpServletResponse response) {
        try {
            hmeProdLinePassRateService.prodLineDayPassRateExport(tenantId, params, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }

    @ApiOperation(value = "日直通率报表导出")
    @PostMapping("/async-day-export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> asyncProdLineDayPassRateExport(@PathVariable("organizationId") Long tenantId,
                                                               HttpServletRequest request,
                                                               HttpServletResponse response,
                                                               @RequestBody HmeProdLinePassRateDTO2 dto) throws IOException {
        hmeProdLinePassRateService.asyncProdLineDayPassRateExport(tenantId, dto, request, response);
        return Results.success();
    }

    @ApiOperation(value = "产品直通率创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      String fileName){
        if (StringUtils.isBlank(fileName)) {
            fileName = FILE_PRODUCT_PASS_RATE;
        }
        HmeExportTaskVO exportTaskVO = hmeProdLinePassRateService.createTask(tenantId, request, response, fileName);
        return Results.success(exportTaskVO);
    }

    @ApiOperation(value = "日直通率创建任务")
    @PostMapping("/create-day-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createDayTask(@PathVariable("organizationId") Long tenantId,
                                                         HttpServletRequest request,
                                                         HttpServletResponse response,
                                                         String fileName){
        if (StringUtils.isBlank(fileName)) {
            fileName = FILE_PRODUCT_DAY_PASS_RATE;
        }
        HmeExportTaskVO exportTaskVO = hmeProdLinePassRateService.createTask(tenantId, request, response, fileName);
        return Results.success(exportTaskVO);
    }
}
