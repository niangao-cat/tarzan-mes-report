package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeWipStocktakeDocDTO15;
import com.ruike.hme.app.service.HmeWipStocktakeDocService;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO11;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO7;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 在制盘点单 管理 API
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
@RestController("hmeWipStocktakeDocController.v1")
@RequestMapping("/v1/{organizationId}/hme-wip-stocktake-docs")
@Slf4j
public class HmeWipStocktakeDocController {

    @Autowired
    private HmeWipStocktakeDocService hmeWipStocktakeDocService;

    @ApiOperation(value = "盘点投料明细汇总")
    @GetMapping(value = "/release-detail", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeWipStocktakeDocVO7>> releaseDetailPageQuery(@PathVariable("organizationId") Long tenantId,
                                                                              HmeWipStocktakeDocDTO15 dto, PageRequest pageRequest) {
        return Results.success(hmeWipStocktakeDocService.releaseDetailPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "在制盘点投料汇总导出")
    @GetMapping(value = "/release-detail/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeWipStocktakeDocVO11.class)
    public ResponseEntity<List<HmeWipStocktakeDocVO11>> releaseDetailExport(@PathVariable("organizationId") Long tenantId,
                                                                            ExportParam exportParam,
                                                                            HttpServletResponse response,
                                                                            HmeWipStocktakeDocDTO15 dto) {
        return Results.success(hmeWipStocktakeDocService.releaseDetailExport(tenantId, dto));
    }
}
