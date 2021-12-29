package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEmployeeAssignDTO;
import com.ruike.hme.app.service.HmeEmployeeAssignReportService;
import com.ruike.hme.domain.vo.HmeEmployeeAssignExportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import org.hzero.core.util.Results;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController("HmeEmployeeAssignReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-staff-attribute-report")
@Api(tags = "员工制造属性报表")
public class HmeEmployeeAssignReportController {

    @Autowired
    private HmeEmployeeAssignReportService employeeAssignReportService;

    @ApiOperation(value = "员工制造属性查看报表")
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeEmployeeAssignExportVO>> list(@PathVariable("organizationId") Long tenantId,
                                                                HmeEmployeeAssignDTO dto,
                                                                @ApiIgnore PageRequest pageRequest) {
        return Results.success(employeeAssignReportService.queryList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "导出员工制造属性报表")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeEmployeeAssignExportVO.class)
    public ResponseEntity<List<HmeEmployeeAssignExportVO>> export(@PathVariable("organizationId") Long tenantId,
                                                                  HmeEmployeeAssignDTO dto,
                                                                  ExportParam exportParam,
                                                                  HttpServletResponse response) {
        return Results.success(employeeAssignReportService.exportReport(tenantId, dto));
    }
}
