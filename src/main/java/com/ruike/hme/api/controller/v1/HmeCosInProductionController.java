package com.ruike.hme.api.controller.v1;


import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.app.service.HmeCosInProductionService;
import com.ruike.hme.domain.repository.HmeCosInProductionRepository;
import com.ruike.hme.domain.vo.HmeCosInNcRecordVO;
import com.ruike.hme.domain.vo.HmeCosInProductionVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * COS在制报表 API管理
 *
 * @author wenqiang.yin@hand-china.com 2021/01/27 13:25
 */
@RestController("hmeCosInProductionController.v1")
@RequestMapping("/v1/{organizationId}/cos-in-production")
@Api(tags = "HmeCosInProductioin")
public class HmeCosInProductionController extends BaseController {

    private final HmeCosInProductionRepository hmeCosInProductionRepository;
    private final HmeCosInProductionService hmeCosInProductionService;
    private final LovAdapter lovAdapter;

    public HmeCosInProductionController(HmeCosInProductionRepository hmeCosInProductionRepository, HmeCosInProductionService hmeCosInProductionService, LovAdapter lovAdapter) {
        this.hmeCosInProductionRepository = hmeCosInProductionRepository;
        this.hmeCosInProductionService = hmeCosInProductionService;
        this.lovAdapter = lovAdapter;
    }

    @ApiOperation(value = "COS在制报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosInProductionVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           HmeCosInProductionDTO dto,
                                                           @ApiIgnore PageRequest pageRequest) {
        this.validObject(dto);
        dto.initParam(tenantId, lovAdapter);
        Page<HmeCosInProductionVO> list = hmeCosInProductionRepository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS在制报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    public ResponseEntity<?> export(@PathVariable("organizationId") Long tenantId,
                                    HmeCosInProductionDTO dto,
                                    HttpServletResponse response) {
        dto.initParam(tenantId, lovAdapter);
        hmeCosInProductionService.export(tenantId, dto, response);
        return Results.success();
    }

    @ApiOperation(value = "COS在制报表-不良弹框")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/nc-record-list")
    public ResponseEntity<Page<HmeCosInNcRecordVO>> ncRecordList(@PathVariable("organizationId") Long tenantId,
                                                                 HmeCosInProductionVO dto,
                                                                 PageRequest pageRequest) {
        Page<HmeCosInNcRecordVO> list = hmeCosInProductionRepository.ncRecordList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS在制报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/async-export")
    public ResponseEntity<?> asyncExport(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeCosInProductionDTO dto) throws IOException {
        dto.initParam(tenantId, lovAdapter);
        hmeCosInProductionService.asyncExport(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response){
        HmeExportTaskVO exportTaskVO = hmeCosInProductionService.createTask(tenantId, request, response);
        return Results.success(exportTaskVO);
    }
}
