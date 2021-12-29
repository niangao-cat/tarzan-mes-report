package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeProductionLineDetailsDTO;
import com.ruike.hme.app.service.HmeProLineDetailsService;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiModelProperty;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产量日明细报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/4/14 19:49
 */
@RestController("HmeProductDetailsController.v1")
@RequestMapping("/v1/{organizationId}/hme_product_details")
public class HmeProductDetailsController {

    @Autowired
    private HmeProLineDetailsService hmeProLineDetailsService;


    @ApiOperation(value = "产量日明细报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-production-line-details")
    public ResponseEntity<Page<HmeProductionLineDetailsDTO>> queryProductionLineDetails(@PathVariable("organizationId") Long tenantId,
                                                                                        HmeProductionLineDetailsVO params,
                                                                                        @ApiIgnore PageRequest pageRequest){
        return Results.success(hmeProLineDetailsService.queryProductionLineDetails(tenantId, pageRequest, params));
    }

    @ApiOperation(value = "产量日明细-班次信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-product-shift-list")
    public ResponseEntity<Page<HmeProductionLineDetailsDTO>> queryProductShiftList(@PathVariable("organizationId") Long tenantId,
                                                                                   @ApiIgnore PageRequest pageRequest, HmeProductionLineDetailsVO params) throws Exception {
        return Results.success(hmeProLineDetailsService.queryProductShiftList(tenantId, pageRequest, params));
    }

    @ApiOperation(value = "产量日明细-投产信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-product-process-eo-list")
    public ResponseEntity<Page<HmeProductEoInfoVO>> queryProductProcessEoList(@PathVariable("organizationId") Long tenantId,
                                                                              @ApiIgnore PageRequest pageRequest, HmeProductEoInfoVO params) {
        return Results.success(hmeProLineDetailsService.queryProductProcessEoList(tenantId, pageRequest, params));
    }

    @ApiOperation(value = "工段产量日明细报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/line-station-details-export")
    @ExcelExport(HmeProductionLineDetailsDTO.class)
    public ResponseEntity<List<HmeProductionLineDetailsDTO>> lineStationDetailsExport(@PathVariable("organizationId") Long tenantId,
                                                                                      HmeProductionLineDetailsVO params,
                                                                                      ExportParam exportParam,
                                                                                      HttpServletResponse response) {
        return Results.success(hmeProLineDetailsService.lineStationDetailsExport(tenantId, params));
    }
}
