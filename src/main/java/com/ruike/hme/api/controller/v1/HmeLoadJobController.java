package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.HmeLoadJobQuery;
import com.ruike.hme.api.dto.representation.HmeLoadJobRept;
import com.ruike.hme.app.service.HmeLoadJobService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
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
 * <p>
 * 芯片装载作业API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 19:02
 */
@RestController("hmeLoadJobController.v1")
@RequestMapping("/v1/{organizationId}/hme-load-jobs")
@Api(SwaggerApiConfig.HME_LOAD_JOB)
public class HmeLoadJobController extends BaseController {

    private final HmeLoadJobService hmeLoadJobService;

    public HmeLoadJobController(HmeLoadJobService hmeLoadJobService) {
        this.hmeLoadJobService = hmeLoadJobService;
    }

    @ApiOperation(value = "芯片装载作业 分页查询")
    @GetMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeLoadJobRept>> pageList(@PathVariable("organizationId") Long tenantId,
                                                         HmeLoadJobQuery dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(hmeLoadJobService.pageList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "芯片装载作业 导出")
    @GetMapping("/export")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeLoadJobRept.class)
    public ResponseEntity<List<HmeLoadJobRept>> export(@PathVariable("organizationId") Long tenantId,
                                                       ExportParam exportParam,
                                                       HttpServletResponse response,
                                                       HmeLoadJobQuery dto) {
        dto.initParam();
        return Results.success(hmeLoadJobService.export(tenantId, dto));
    }
}
