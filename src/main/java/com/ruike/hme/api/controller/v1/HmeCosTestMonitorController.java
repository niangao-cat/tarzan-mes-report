package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosTestMonitorDTO;
import com.ruike.hme.domain.repository.HmeCosTestMonitorRepository;
import com.ruike.hme.domain.vo.HmeCosTestMonitorVO;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
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

/**
 * COS良率监控报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/11/8 16:34
 */
@RestController("hmeCosTestMonitorController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-test-monitor")
public class HmeCosTestMonitorController {

    @Autowired
    private HmeCosTestMonitorRepository hmeCosTestMonitorRepository;

    @ApiOperation(value = "COS良率监控报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-record-List")
    public ResponseEntity<Page<HmeCosTestMonitorVO>> queryRecordList(@PathVariable("organizationId") Long tenantId,
                                                                     HmeCosTestMonitorDTO dto,
                                                                     @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        Page<HmeCosTestMonitorVO> list = hmeCosTestMonitorRepository.queryRecordList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS良率监控报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosTestMonitorVO.class)
    public ResponseEntity<List<HmeCosTestMonitorVO>> export(@PathVariable("organizationId") Long tenantId,
                                                       HmeCosTestMonitorDTO dto,
                                                       ExportParam exportParam,
                                                       HttpServletResponse response) {
        dto.initParam();
        return Results.success(hmeCosTestMonitorRepository.export(tenantId, dto));
    }

    @ApiOperation(value = "COS良率监控报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/async-export")
    public ResponseEntity<?> asyncExport(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeCosTestMonitorDTO dto) throws IOException {
        dto.initParam();
        hmeCosTestMonitorRepository.asyncExport(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response){
        HmeExportTaskVO exportTaskVO = hmeCosTestMonitorRepository.createTask(tenantId, request, response);
        return Results.success(exportTaskVO);
    }

}
