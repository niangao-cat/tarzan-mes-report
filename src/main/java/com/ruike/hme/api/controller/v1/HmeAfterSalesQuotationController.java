package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeAfterSalesQuotationDTO;
import com.ruike.hme.app.service.HmeAfterSalesQuotationService;
import com.ruike.hme.domain.vo.HmeAfterSalesQuotationVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
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
 * description  售后报价单报表
 *
 * @author wengang.qiang@hand-china 2021/10/15 14:12
 */
@RestController("HmeAfterSalesQuotationController.v1")
@RequestMapping("/v1/{organizationId}/Hme-after-sales-quotation")
public class HmeAfterSalesQuotationController {

    private final HmeAfterSalesQuotationService hmeAfterSalesQuotationService;

    public HmeAfterSalesQuotationController(HmeAfterSalesQuotationService hmeAfterSalesQuotationService) {
        this.hmeAfterSalesQuotationService = hmeAfterSalesQuotationService;
    }

    @ApiOperation(value = "售后报价单报表查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-after-sales")
    public ResponseEntity<Page<HmeAfterSalesQuotationVO>> querySalesQuotation(@PathVariable(value = "organizationId") Long tenantId,
                                                                              PageRequest pageRequest,
                                                                              HmeAfterSalesQuotationDTO dto) {
        dto.initParamSAPQuotationNo();
        dto.initParamProductCode();
        dto.initParamSerialNumber();
        return Results.success(hmeAfterSalesQuotationService.query(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "售后登记查询报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeAfterSalesQuotationVO.class)
    public ResponseEntity<List<HmeAfterSalesQuotationVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                 ExportParam exportParam,
                                                                 HttpServletResponse response,
                                                                 HmeAfterSalesQuotationDTO dto) {
        List<HmeAfterSalesQuotationVO> result = hmeAfterSalesQuotationService.export(tenantId, dto);
        return Results.success(result);
    }
}
