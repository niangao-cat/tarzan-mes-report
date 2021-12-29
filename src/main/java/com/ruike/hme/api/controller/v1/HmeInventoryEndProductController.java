package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeInventoryEndProductRepository;
import com.ruike.hme.domain.vo.HmeInventoryEndProductVO;
import com.ruike.hme.domain.vo.HmeInventoryEndProductVO2;
import com.ruike.hme.domain.vo.HmeInventoryEndProductVO3;
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
 * 售后在制品盘点-成品报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/4/1 15:17
 */
@RestController("HmeInventoryEndProductController.v1")
@RequestMapping("/v1/{organizationId}/hme-inventory-end-product")
@Api(tags = SwaggerApiConfig.HME_INVENTORY_END_PRODUCT)
public class HmeInventoryEndProductController {


    @Autowired
    private HmeInventoryEndProductRepository hmeInventoryEndProductRepository;

    @ApiOperation(value = "售后在制品盘点-成品报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/inventory-end-product-query")
    public ResponseEntity<Page<HmeInventoryEndProductVO2>> inventoryEndProductQuery(@PathVariable(value = "organizationId") Long tenantId,
                                                                                    HmeInventoryEndProductVO dto,
                                                                                    @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        return Results.success(hmeInventoryEndProductRepository.inventoryEndProductQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "售后在制品盘点-成品报表导出")
    @GetMapping(value = "/inventory-end-product-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeInventoryEndProductVO3.class)
    public ResponseEntity<List<HmeInventoryEndProductVO3>> inventoryEndProductExport(@PathVariable("organizationId") Long tenantId,
                                                                                     HmeInventoryEndProductVO dto,
                                                                                     HttpServletResponse response,
                                                                                     ExportParam exportParam) {
        dto.initParam();
        return Results.success(hmeInventoryEndProductRepository.inventoryEndProductExport(tenantId, dto));
    }


}
