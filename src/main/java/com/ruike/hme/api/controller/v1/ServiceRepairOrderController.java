package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.ServiceRepairOrderQuery;
import com.ruike.hme.api.dto.representation.ServiceRepairOrderRepresentation;
import com.ruike.hme.domain.repository.ServiceRepairOrderRepository;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 维修订单查看报表 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/27 11:47
 */
@RestController("serviceRepairOrderController.v1")
@RequestMapping("/v1/{organizationId}/repair-order")
public class ServiceRepairOrderController extends BaseController {
    private final ServiceRepairOrderRepository repository;

    public ServiceRepairOrderController(ServiceRepairOrderRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "维修订单查看报表 列表查询")
    @GetMapping(produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<ServiceRepairOrderRepresentation>> page(@PathVariable("organizationId") Long tenantId,
                                                                       ServiceRepairOrderQuery dto,
                                                                       PageRequest pageRequest) {
        this.validObject(dto);
        dto.initParam(tenantId);
        return Results.success(repository.pagedList(dto, pageRequest));
    }

    @ApiOperation(value = "维修订单查看报表 导出")
    @GetMapping(value = {"/export"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(ServiceRepairOrderRepresentation.class)
    public ResponseEntity<List<ServiceRepairOrderRepresentation>> export(@PathVariable("organizationId") Long tenantId,
                                                                         ServiceRepairOrderQuery dto,
                                                                         ExportParam exportParam,
                                                                         HttpServletResponse response) {
        this.validObject(dto);
        dto.initParam(tenantId);
        return Results.success(repository.export(dto));
    }
}
