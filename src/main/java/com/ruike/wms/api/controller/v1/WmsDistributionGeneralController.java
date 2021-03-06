package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsDistributionGeneralQueryDTO;
import com.ruike.wms.app.service.WmsDistributionGeneralService;
import com.ruike.wms.domain.repository.WmsDistributionGeneralRepository;
import com.ruike.wms.domain.vo.WmsDistributionGeneralVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 配送综合查询报表 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2020/12/24 17:13
 */
@RestController("wmsDistributionGeneralController.v1")
@RequestMapping("/v1/{organizationId}/distribution-general")
@Api(tags = "WmsDistributionGeneral")
public class WmsDistributionGeneralController {

    private final WmsDistributionGeneralRepository wmsDistributionGeneralRepository;
    private final WmsDistributionGeneralService wmsDistributionGeneralService;

    public WmsDistributionGeneralController(WmsDistributionGeneralRepository wmsDistributionGeneralRepository, WmsDistributionGeneralService wmsDistributionGeneralService) {
        this.wmsDistributionGeneralRepository = wmsDistributionGeneralRepository;
        this.wmsDistributionGeneralService = wmsDistributionGeneralService;
    }

    @ApiOperation(value = "配送综合查询报表 查询列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<WmsDistributionGeneralVO>> list(@PathVariable("organizationId") Long tenantId,
                                                               WmsDistributionGeneralQueryDTO dto,
                                                               @ApiIgnore PageRequest pageRequest) {
        Page<WmsDistributionGeneralVO> list = wmsDistributionGeneralRepository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "配送综合查询报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    public ResponseEntity<?> export(@PathVariable("organizationId") Long tenantId,
                                    WmsDistributionGeneralQueryDTO dto,
                                    HttpServletResponse response) {
        wmsDistributionGeneralService.export(tenantId, dto, response);
        return Results.success();
    }

}
