package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.CosCompletionDetailQuery;
import com.ruike.hme.api.dto.representation.CosCompletionDetailRepresentation;
import com.ruike.hme.domain.repository.CosCompletionDetailRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * COS完工芯片明细报表 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/28 15:46
 */
@RestController("cosCompletionDetailController.v1")
@RequestMapping("/v1/{organizationId}/cos-completion-detail")
public class CosCompletionDetailController extends BaseController {
    private final CosCompletionDetailRepository repository;
    private final LovAdapter lovAdapter;

    public CosCompletionDetailController(CosCompletionDetailRepository repository, LovAdapter lovAdapter) {
        this.repository = repository;
        this.lovAdapter = lovAdapter;
    }

    @ApiOperation(value = "COS完工芯片明细报表 分页查询")
    @PostMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<CosCompletionDetailRepresentation>> page(@PathVariable("organizationId") Long tenantId,
                                                                        @RequestBody CosCompletionDetailQuery dto,
                                                                        PageRequest pageRequest) {
        this.validObject(dto);
        dto.initParam(tenantId, lovAdapter);
        return Results.success(repository.pagedList(dto, pageRequest));
    }

    @ApiOperation(value = "COS完工芯片明细报表 导出")
    @PostMapping(value = {"/export"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(CosCompletionDetailRepresentation.class)
    public ResponseEntity<List<CosCompletionDetailRepresentation>> export(@PathVariable("organizationId") Long tenantId,
                                                                          @RequestBody CosCompletionDetailQuery dto,
                                                                          ExportParam exportParam,
                                                                          HttpServletResponse response) {
        this.validObject(dto);
        dto.initParam(tenantId, lovAdapter);
        return Results.success(repository.list(dto));
    }
}
