package com.ruike.qms.api.controller.v1;


import com.ruike.qms.api.dto.QmsIqcInspectionKanbanQueryDTO;
import com.ruike.qms.api.dto.QmsSupplierQualityQueryDTO;
import com.ruike.qms.domain.repository.QmsIqcInspectionKanbanRepository;
import com.ruike.qms.domain.vo.ChartsSquareResultVO;
import com.ruike.qms.domain.vo.QmsIqcInspectionKanbanVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * <p>
 * IQC检验看板 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/28 11:05
 */
@RestController("QmsIqcInspectionKanbanController.v1")
@RequestMapping("/v1/{organizationId}/iqc-inspection-kanban")
public class QmsIqcInspectionKanbanController {
    private final QmsIqcInspectionKanbanRepository qmsIqcInspectionKanbanRepository;

    public QmsIqcInspectionKanbanController(QmsIqcInspectionKanbanRepository qmsIqcInspectionKanbanRepository) {
        this.qmsIqcInspectionKanbanRepository = qmsIqcInspectionKanbanRepository;
    }

    @ApiOperation(value = "IQC日常工作计划报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/kanban")
    public ResponseEntity<Page<QmsIqcInspectionKanbanVO>> kanbanPage(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody QmsIqcInspectionKanbanQueryDTO dto,
                                                                     @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.pagedKanbanList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "IQC日常工作计划报表 列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/kanban/list")
    public ResponseEntity<List<QmsIqcInspectionKanbanVO>> kanbanList(@PathVariable("organizationId") Long tenantId,
                                                                     @RequestBody QmsIqcInspectionKanbanQueryDTO dto) {
        List<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.kanbanListGet(tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "供应商来料在线质量 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/quality")
    public ResponseEntity<Page<QmsIqcInspectionKanbanVO>> qualityPage(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody QmsSupplierQualityQueryDTO dto,
                                                                      @ApiIgnore PageRequest pageRequest) {
        Page<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.pagedQualityList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "供应商来料在线质量 列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/quality/list")
    public ResponseEntity<List<QmsIqcInspectionKanbanVO>> qualityList(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody QmsSupplierQualityQueryDTO dto) {
        List<QmsIqcInspectionKanbanVO> list = qmsIqcInspectionKanbanRepository.qualityListGet(tenantId, dto);
        return Results.success(list);
    }

    @ApiOperation(value = "供应商来料在线质量 图表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/quality/chart")
    public ResponseEntity<ChartsSquareResultVO> qualityChartMap(@PathVariable("organizationId") Long tenantId,
                                                                @RequestBody QmsSupplierQualityQueryDTO dto) {
        ChartsSquareResultVO result = qmsIqcInspectionKanbanRepository.qualityChartMapGet(tenantId, dto);
        return Results.success(result);
    }

    @ApiOperation(value = "IQC日常工作计划报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/kanban-export")
    @ExcelExport(QmsIqcInspectionKanbanVO.class)
    public ResponseEntity<List<QmsIqcInspectionKanbanVO>> kanbanExport(@PathVariable("organizationId") Long tenantId,
                                                                     QmsIqcInspectionKanbanQueryDTO dto,
                                                                     ExportParam exportParam,
                                                                     HttpServletResponse response) {
        return Results.success(qmsIqcInspectionKanbanRepository.kanbanExport(tenantId, dto));
    }

    @ApiOperation(value = "供应商来料在线质量导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/quality-export")
    public ResponseEntity<?> qualityExport(@PathVariable("organizationId") Long tenantId,
                                                                        QmsSupplierQualityQueryDTO dto,
                                                                        HttpServletResponse response) {
        qmsIqcInspectionKanbanRepository.qualityExport(tenantId, dto, response);
        return Results.success();
    }

}
