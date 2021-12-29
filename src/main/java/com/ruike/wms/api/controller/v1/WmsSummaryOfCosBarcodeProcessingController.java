package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsSummaryOfCosBarcodeProcessingDTO;
import com.ruike.wms.domain.repository.WmsSummaryOfCosBarcodeProcessingRepository;
import com.ruike.wms.domain.vo.WmsSummaryOfCosBarcodeProcessingVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description COS条码加工汇总表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2020/11/18
 * @time 14:35
 * @version 0.0.1
 * @return
 */
@RestController("WmsSummaryOfCosBarcodeProcessingController.v1")
@RequestMapping("/v1/{organizationId}/wms-summary-of-cos-barcode-processing-repository")
public class WmsSummaryOfCosBarcodeProcessingController {

    @Autowired
    private WmsSummaryOfCosBarcodeProcessingRepository wmsSummaryOfCosBarcodeProcessingRepository;

    @ApiOperation(value = "查询报表信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<WmsSummaryOfCosBarcodeProcessingVO>> list(@PathVariable("organizationId") Long tenantId,
                                                                         WmsSummaryOfCosBarcodeProcessingDTO dto, PageRequest pageRequest) {
        return Results.success(wmsSummaryOfCosBarcodeProcessingRepository.list(tenantId, pageRequest, dto));
    }

    @ApiOperation(value = "COS条码加工汇总表-导出")
    @PostMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @ProcessLovValue
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(WmsSummaryOfCosBarcodeProcessingVO.class)
    public ResponseEntity<List<WmsSummaryOfCosBarcodeProcessingVO>> listForExport(@PathVariable("organizationId") Long tenantId,
                                                               ExportParam exportParam,
                                                               HttpServletResponse response,
                                                               @RequestBody WmsSummaryOfCosBarcodeProcessingDTO condition) {
        return Results.success(wmsSummaryOfCosBarcodeProcessingRepository.listForExport(tenantId, condition));
    }
}
