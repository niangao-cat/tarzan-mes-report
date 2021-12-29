package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeDistributionDemandQueryDTO;
import com.ruike.hme.api.dto.HmeProdLinePassRateDTO;
import com.ruike.hme.app.service.HmeDistributionDemandService;
import com.ruike.hme.domain.vo.HmeDistributionDemandRepresentationVO;
import com.ruike.hme.domain.vo.HmeProdLinePassRateVO5;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.SwaggerConfig;
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
 * 配送需求滚动报表 管理API
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 04:22:14
 */
@RestController("hmeDistributionDemandController.v1")
@RequestMapping("/v1/{organizationId}/hme-distribution-demands")
@Api(SwaggerApiConfig.HME_DISTRIBUTION_DEMAND)
public class HmeDistributionDemandController extends BaseController {

    private final HmeDistributionDemandService service;

    public HmeDistributionDemandController(HmeDistributionDemandService service) {
        this.service = service;
    }

    @ApiOperation(value = "配送需求滚动报表 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeDistributionDemandRepresentationVO>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                                            @ApiIgnore PageRequest pageRequest,
                                                                            HmeDistributionDemandQueryDTO query) {
        this.validObject(query);
        query.paramInit();
        return Results.success(service.page(tenantId, pageRequest, query));
    }

    @ApiOperation(value = "配送需求滚动报表 导出列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeDistributionDemandRepresentationVO.class)
    public ResponseEntity<List<HmeDistributionDemandRepresentationVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                              ExportParam exportParam,
                                                                              HttpServletResponse response,
                                                                              HmeDistributionDemandQueryDTO query) {
        this.validObject(query);
        query.paramInit();
        return Results.success(service.export(tenantId, query, exportParam));
    }
}
