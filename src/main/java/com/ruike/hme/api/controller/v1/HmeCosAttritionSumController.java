package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosAttritionSumDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumRepresentationDTO;
import com.ruike.hme.app.service.HmeCosAttritionSumService;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
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
 * COS损耗汇总报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/30 9:58
 */
@RestController("hmeCosAttritionSumController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-attrition-sum")
public class HmeCosAttritionSumController {

    @Autowired
    private HmeCosAttritionSumService hmeCosAttritionSumService;

    @ApiOperation(value = "COS损耗汇总报表 分页列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosAttritionSumDTO>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                            @ApiIgnore PageRequest pageRequest,
                                                            WorkOrderAttritionSumQueryDTO query) {
        query.paramInit();
        return Results.success(hmeCosAttritionSumService.page(tenantId, query, pageRequest));
    }

    @ApiOperation(value = "COS损耗汇总报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosAttritionSumDTO.class)
    public ResponseEntity<List<HmeCosAttritionSumDTO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                               ExportParam exportParam,
                                                                               HttpServletResponse response,
                                                                               WorkOrderAttritionSumQueryDTO query) {
        query.paramInit();
        return Results.success(hmeCosAttritionSumService.export(tenantId, query));
    }

}
