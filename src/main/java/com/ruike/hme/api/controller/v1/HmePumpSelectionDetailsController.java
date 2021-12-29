package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosFunctionDTO2;
import com.ruike.hme.api.dto.HmeInputRecordDTO;
import com.ruike.hme.api.dto.HmePumpSelectionDetailsDTO;
import com.ruike.hme.app.service.HmePumpSelectionDetailsService;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeInputRecordVO;
import com.ruike.hme.domain.vo.HmePumpSelectionDetailsVO;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 泵浦源预筛选报表管理API
 *
 * @author: chaonan.hu@hand-china.com 2021-11-05 09:17:20
 **/
@RestController("hmePumpSelectionDetailsController.v1")
@RequestMapping("/v1/{organizationId}/hme-pump-selection-details")
public class HmePumpSelectionDetailsController {

    @Autowired
    private HmePumpSelectionDetailsService hmePumpSelectionDetailsService;

    private static final String FILE_PUMP_PRE_SELECTION = "泵浦源预筛选报表";

    @ApiOperation(value = "泵浦源预筛选报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/page-query")
    public ResponseEntity<Page<HmePumpSelectionDetailsVO>> pumpSelectionDetailsPageQuery(@PathVariable(value = "organizationId")  Long tenantId,
                                                                                         HmePumpSelectionDetailsDTO dto, PageRequest pageRequest){
        return Results.success(hmePumpSelectionDetailsService.pumpSelectionDetailsPageQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "泵浦源预筛选报表报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmePumpSelectionDetailsVO.class)
    public ResponseEntity<List<HmePumpSelectionDetailsVO>> pumpSelectionDetailsExport(@PathVariable(value = "organizationId")  Long tenantId,
                                                                    HmePumpSelectionDetailsDTO dto,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response){
        return Results.success(hmePumpSelectionDetailsService.pumpSelectionDetailsExport(tenantId, dto));
    }
}
