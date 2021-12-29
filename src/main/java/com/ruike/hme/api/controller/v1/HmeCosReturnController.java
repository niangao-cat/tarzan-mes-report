package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosInProductionDTO;
import com.ruike.hme.api.dto.HmeCosReturnDTO;
import com.ruike.hme.domain.repository.HmeCosReturnRepository;
import com.ruike.hme.domain.vo.HmeCosInProductionVO;
import com.ruike.hme.domain.vo.HmeCosReturnVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/***
 * COS退料报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/11/3
 */
@RestController("hmeCosReturnController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-return")
@Api(tags = "HmeCosReturn")
public class HmeCosReturnController {

    @Autowired
    private HmeCosReturnRepository hmeCosReturnRepository;

    @ApiOperation(value = "COS退料报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-record-List")
    public ResponseEntity<Page<HmeCosReturnVO>> queryRecordList(@PathVariable("organizationId") Long tenantId,
                                                           HmeCosReturnDTO dto,
                                                           @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        Page<HmeCosReturnVO> list = hmeCosReturnRepository.queryRecordList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS退料报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosReturnVO.class)
    public ResponseEntity<List<HmeCosReturnVO>> export(@PathVariable("organizationId") Long tenantId,
                                                       HmeCosReturnDTO dto,
                                                       ExportParam exportParam,
                                                       HttpServletResponse response) {
        dto.initParam();
        return Results.success(hmeCosReturnRepository.export(tenantId, dto));
    }

    @ApiOperation(value = "COS退料报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/async-export")
    public ResponseEntity<?> asyncExport(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeCosReturnDTO dto) throws IOException {
        dto.initParam();
        hmeCosReturnRepository.asyncExport(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response){
        HmeExportTaskVO exportTaskVO = hmeCosReturnRepository.createTask(tenantId, request, response);
        return Results.success(exportTaskVO);
    }

}



