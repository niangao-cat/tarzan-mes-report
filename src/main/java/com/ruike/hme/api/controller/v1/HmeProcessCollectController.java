package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.HmeProcessCollectQuery;
import com.ruike.hme.domain.repository.HmeProcessCollectRepository;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeProcessCollectTitleVO;
import com.ruike.hme.domain.vo.HmeProcessJobDetailVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 工序采集项报表 API
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/15 14:18
 */
@RestController("hmeProcessCollectController.v1")
@RequestMapping("/v1/{organizationId}/hme-process-collect")
@Api(SwaggerApiConfig.HME_PROCESS_COLLECT)
@Slf4j
public class HmeProcessCollectController extends BaseController {

    private final HmeProcessCollectRepository repository;
    private final LovAdapter lovAdapter;

    public HmeProcessCollectController(HmeProcessCollectRepository repository, LovAdapter lovAdapter) {
        this.repository = repository;
        this.lovAdapter = lovAdapter;
    }

    @ApiOperation(value = "工序采集项报表 分页查询")
    @PostMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeProcessCollectTitleVO> pageList(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeProcessCollectQuery dto,
                                                             PageRequest pageRequest) {
        dto.validParam();
        return Results.success(repository.pagedList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序采集项报表 详情列表")
    @GetMapping("/detail/{jobId}")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeProcessJobDetailVO>> pagedJobList(@PathVariable("organizationId") Long tenantId,
                                                                    @PathVariable("jobId") String jobId,
                                                                    PageRequest pageRequest) {
        return Results.success(repository.pagedJobList(tenantId, jobId, pageRequest));
    }

    @ApiOperation(value = "工序采集项报表 导出")
    @PostMapping("/export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> export(@PathVariable("organizationId") Long tenantId,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody HmeProcessCollectQuery dto) {
        dto.validParam();
        repository.export(tenantId, dto, request, response);
        return Results.success();
    }

    @ApiOperation(value = "工序采集项报表异步导出")
    @PostMapping("/async-export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> asyncExport(@PathVariable("organizationId") Long tenantId,
                                              @RequestBody HmeProcessCollectQuery dto) throws IOException {
        dto.validParam();
        List<LovValueDTO> qualityStatusLov = lovAdapter.queryLovValue("HME.QUALITY_STATUS", tenantId);
        List<LovValueDTO> flagYnLov = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
        repository.asyncExport(tenantId, dto, qualityStatusLov, flagYnLov);
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

    @ApiOperation(value = "工序采集项报表分页查询-GP")
    @PostMapping("/gp/list")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeProcessCollectTitleVO> gpPageList(@PathVariable("organizationId") Long tenantId,
                                                             @RequestBody HmeProcessCollectQuery dto,
                                                             PageRequest pageRequest) {
        dto.validParam();
        return Results.success(repository.gpPagedList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序采集项报表导出-GP")
    @PostMapping("/gp/export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Void> gpExport(@PathVariable("organizationId") Long tenantId,
                                         HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody HmeProcessCollectQuery dto) {
        dto.validParam();
        repository.gpExport(tenantId, dto, request, response);
        return Results.success();
    }

    @ApiOperation(value = "工序采集项报表异步导出-GP")
    @PostMapping("/gp/async-export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<?> gpAsyncExport(@PathVariable("organizationId") Long tenantId,
                                           @RequestBody HmeProcessCollectQuery dto) throws IOException {
        dto.validParam();
        repository.gpAsyncExport(tenantId, dto);
        return Results.success();
    }
}
