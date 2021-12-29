package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeWorkCellDetailsReportRepository;
import com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO;
import com.ruike.hme.domain.vo.HmeWorkCellDetailsReportVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 工位产量明细报表
 *
 * @author xin.tian@raycuslaser 2021/06/18 14:31
 */
@RestController("hmeWorkCellDetailsReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-work-cell-details-report")
@Api(tags = SwaggerApiConfig.HME_WORK_CELL_DETAILS_REPORT)
public class HmeWorkCellDetailsReportController {

    private final HmeWorkCellDetailsReportRepository hmeWorkCellDetailsReportRepository;

    public HmeWorkCellDetailsReportController(HmeWorkCellDetailsReportRepository hmeWorkCellDetailsReportRepository) {
        this.hmeWorkCellDetailsReportRepository = hmeWorkCellDetailsReportRepository;
    }


    @ApiOperation(value = "工位产量明细报表查询")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWorkCellDetailsReportVO2>> listForUi(
            @PathVariable("organizationId") Long tenantId, HmeWorkCellDetailsReportVO dto, PageRequest pageRequest) {
        dto.initialization();
        return Results.success(hmeWorkCellDetailsReportRepository.queryWorkCellReportList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工位产量明细报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/export")
    @ExcelExport(HmeWorkCellDetailsReportVO2.class)
    public ResponseEntity<List<HmeWorkCellDetailsReportVO2>> export(@PathVariable("organizationId") Long tenantId,
                                                                    HmeWorkCellDetailsReportVO dto,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response ) {
        dto.initialization();
        return Results.success(hmeWorkCellDetailsReportRepository.exportWorkCellReportList(tenantId, dto));
    }
}
