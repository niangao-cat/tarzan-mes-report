package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeExceptionReportService;
import com.ruike.hme.domain.vo.HmeExceptionReportVO;
import com.ruike.hme.domain.vo.HmeExceptionReportVO2;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 异常信息报表及导出
 *
 * @author sanfeng.zhang@hand-china.com 2021/4/14 9:37
 */
@RestController("HmeExceptionReportController.v1")
@RequestMapping("/v1/{organizationId}/hme-common-report")
@Api(tags = SwaggerApiConfig.HME_DEMO)
public class HmeExceptionReportController {

    @Autowired
    private HmeExceptionReportService hmeExceptionReportService;

    @ApiOperation(value = "异常信息查看报表")
    @GetMapping(value = "/exception-report-list", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeExceptionReportVO2>> queryExceptionReportList(@PathVariable("organizationId") Long tenantId,
                                                                                HmeExceptionReportVO dto,
                                                                                @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeExceptionReportService.queryExceptionReportList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "异常信息查看报表导出")
    @GetMapping(value = "/exception-report-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeExceptionReportVO2.class)
    public ResponseEntity<List<HmeExceptionReportVO2>> queryExceptionReportExport(@PathVariable("organizationId") Long tenantId,
                                                                                  HmeExceptionReportVO dto,
                                                                                  ExportParam exportParam,
                                                                                  HttpServletResponse response) {
        return Results.success(hmeExceptionReportService.queryExceptionReportExport(tenantId, dto));
    }
}
