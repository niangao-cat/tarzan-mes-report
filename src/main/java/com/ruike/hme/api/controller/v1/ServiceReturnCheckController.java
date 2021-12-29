package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.ServiceReturnCheckQueryDTO;
import com.ruike.hme.api.dto.ServiceReturnCheckRepresentationDTO;
import com.ruike.hme.app.service.ServiceReturnCheckService;
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
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 售后退库检测报表 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/22 14:17
 */
@RestController("serviceReturnCheckController.v1")
@RequestMapping("/v1/{organizationId}/service-return-check")
@Api(SwaggerApiConfig.SERVICE_RETURN_CHECK)
public class ServiceReturnCheckController extends BaseController {

    private final ServiceReturnCheckService service;

    public ServiceReturnCheckController(ServiceReturnCheckService service) {
        this.service = service;
    }

    @ApiOperation(value = "售后退库检测报表 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<ServiceReturnCheckRepresentationDTO>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                                          @ApiIgnore PageRequest pageRequest,
                                                                          ServiceReturnCheckQueryDTO query) {
        this.validObject(query);
        query.paramInit();
        return Results.success(service.page(tenantId, query, pageRequest));
    }

    @ApiOperation(value = "售后退库检测报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(ServiceReturnCheckRepresentationDTO.class)
    public ResponseEntity<List<ServiceReturnCheckRepresentationDTO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                            ExportParam exportParam,
                                                                            HttpServletResponse response,
                                                                            ServiceReturnCheckQueryDTO query) {
        this.validObject(query);
        query.paramInit();
        return Results.success(service.export(tenantId, query, exportParam));
    }
}
