package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.api.dto.query.HmeProductionFlowQuery;
import com.ruike.hme.api.dto.representation.HmeProductionFlowRepresentation;
import com.ruike.hme.domain.repository.HmeProductionFlowRepository;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 生产流转查询报表
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/25 16:33
 */
@RestController("WmsProductionFlowQueryReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-production-flow")
public class HmeProductionFlowController {
    private final HmeProductionFlowRepository repository;

    public HmeProductionFlowController(HmeProductionFlowRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "生产流转查询报表 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Page<HmeProductionFlowRepresentation>> pagedList(@PathVariable("organizationId") Long tenantId,
                                                                           @RequestBody HmeProductionFlowQuery dto,
                                                                           @ApiIgnore PageRequest pageRequest) {
        dto.initialization();
        return Results.success(repository.pagedList(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "生产流转查询报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping(value = "/export")
    @ExcelExport(HmeProductionFlowRepresentation.class)
    public ResponseEntity<List<HmeProductionFlowRepresentation>> export(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody HmeProductionFlowQuery dto,
                                                                        ExportParam exportParam,
                                                                        HttpServletResponse response) {
        dto.initialization();
        return Results.success(repository.export(tenantId, dto));
    }

    @ApiOperation(value = "生产流转查询报表异步导出")
    @PostMapping("/async-export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> asyncExport(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeProductionFlowQuery dto) throws IOException {
        dto.initialization();
        repository.asyncExport(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      String fileName){
        HmeExportTaskVO exportTaskVO = repository.createTask(tenantId, request, response, fileName);
        return Results.success(exportTaskVO);
    }
}
