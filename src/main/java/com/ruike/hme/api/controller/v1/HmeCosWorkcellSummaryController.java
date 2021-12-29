package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.HmeCosWorkcellSummaryQuery;
import com.ruike.hme.api.dto.representation.HmeCosWorkcellSummaryRepresentation;
import com.ruike.hme.domain.repository.HmeCosWorkcellSummaryRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * COS工位加工汇总 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/26 10:35
 */
@RestController("hmeCosWorkcellSummaryController.v1")
@RequestMapping("/v1/{organizationId}/cos-workcell-summary")
@Api(tags = "HmeCosWorkcellSummary")
public class HmeCosWorkcellSummaryController extends BaseController {
    private final HmeCosWorkcellSummaryRepository repository;
    private final LovAdapter lovAdapter;

    public HmeCosWorkcellSummaryController(HmeCosWorkcellSummaryRepository repository, LovAdapter lovAdapter) {
        this.repository = repository;
        this.lovAdapter = lovAdapter;
    }

    @ApiOperation(value = "COS工位加工汇总 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosWorkcellSummaryRepresentation>> list(@PathVariable("organizationId") Long tenantId,
                                                                          HmeCosWorkcellSummaryQuery dto,
                                                                          @ApiIgnore PageRequest pageRequest) {
        this.validObject(dto);
        dto.initParam(tenantId, lovAdapter);
        Page<HmeCosWorkcellSummaryRepresentation> list = repository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS工位加工汇总 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosWorkcellSummaryRepresentation.class)
    public ResponseEntity<List<HmeCosWorkcellSummaryRepresentation>> export(@PathVariable("organizationId") Long tenantId,
                                                                            HmeCosWorkcellSummaryQuery dto,
                                                                            ExportParam exportParam,
                                                                            HttpServletResponse response) {
        this.validObject(dto);
        dto.initParam(tenantId, lovAdapter);
        List<HmeCosWorkcellSummaryRepresentation> list = repository.export(tenantId, dto);
        return Results.success(list);
    }
}
