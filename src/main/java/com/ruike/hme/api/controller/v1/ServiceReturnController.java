package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.ServiceReturnQuery;
import com.ruike.hme.api.dto.representation.ServiceReturnRepresentation;
import com.ruike.hme.domain.repository.ServiceReturnRepository;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 售后退库查询 资源库实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 15:57
 */
@RestController("serviceReturnController.v1")
@RequestMapping("/v1/{organizationId}/service-return")
public class ServiceReturnController extends BaseController {
    private final ServiceReturnRepository repository;

    public ServiceReturnController(ServiceReturnRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "售后退库查询 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ServiceReturnRepresentation>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                                  @ApiIgnore PageRequest pageRequest,
                                                                  ServiceReturnQuery query) {
        this.validObject(query);
        query.initParam(tenantId);
        return Results.success(repository.pagedList(query, pageRequest));
    }

    @ApiOperation(value = "售后退库查询 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(ServiceReturnRepresentation.class)
    public ResponseEntity<List<ServiceReturnRepresentation>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response,
                                                                    ServiceReturnQuery query) {
        this.validObject(query);
        query.initParam(tenantId);
        return Results.success(repository.list(query));
    }
}
