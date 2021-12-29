package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmePreparationSurplusChipDTO;
import com.ruike.hme.app.service.HmePreparationSurplusChipService;
import com.ruike.hme.domain.vo.HmePreparationSurplusChipVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
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
 * COS筛选剩余芯片统计报表
 *
 * @author: chaonan.hu@hand-china.com 2021-05-07 10:51:21
 **/
@RestController("hmePreparationSurplusChipController.v1")
@RequestMapping("/v1/{organizationId}/hme-preparation-surplus-chip")
public class HmePreparationSurplusChipController extends BaseController {

    @Autowired
    private HmePreparationSurplusChipService hmePreparationSurplusChipService;

    @ApiOperation(value = "分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/list", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Page<HmePreparationSurplusChipVO>> listQuery(@PathVariable("organizationId") Long tenantId,
                                                                       HmePreparationSurplusChipDTO dto, PageRequest pageRequest) {
        return Results.success(hmePreparationSurplusChipService.listQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmePreparationSurplusChipVO.class)
    public ResponseEntity<List<HmePreparationSurplusChipVO>> export(@PathVariable("organizationId") Long tenantId,
                                                               ExportParam exportParam,
                                                               HttpServletResponse response,
                                                               HmePreparationSurplusChipDTO condition) {
        return Results.success(hmePreparationSurplusChipService.export(tenantId, condition));
    }
}
