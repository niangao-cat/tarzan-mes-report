package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosReturnDTO;
import com.ruike.hme.api.dto.HmeInputRecordDTO;
import com.ruike.hme.app.service.HmeInputRecordService;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeInputRecordVO;
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
import org.springframework.web.bind.annotation.*;
import org.xhtmlrenderer.css.parser.property.PrimitivePropertyBuilders;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 投料汇总报表 管理API
 *
 * @author: chaonan.hu@hand-china.com 2021-02-26 10:22:12
 **/

@RestController("hmeInputRecordController.v1")
@RequestMapping("/v1/{organizationId}/hme-input_record")
@Api(tags = SwaggerApiConfig.HME_DEMO)
public class HmeInputRecordController {

    @Autowired
    private HmeInputRecordService hmeInputRecordService;

    @ApiOperation(value = "投料报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping
    public ResponseEntity<Page<HmeInputRecordVO>> inputRecord(@PathVariable(value = "organizationId")  Long tenantId,
                                                              @RequestBody HmeInputRecordDTO dto,
                                                              PageRequest pageRequest){
        dto.initParam();
        return Results.success(hmeInputRecordService.inputRecord(tenantId, dto,pageRequest));
    }

    @ApiOperation(value = "投料报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/input-record-export")
    @ExcelExport(HmeInputRecordVO.class)
    public ResponseEntity<List<HmeInputRecordVO>> inputRecordExport(@PathVariable(value = "organizationId")  Long tenantId,
                                                                    @RequestBody HmeInputRecordDTO dto,
                                                                    ExportParam exportParam,
                                                                    HttpServletResponse response){
        dto.initParam();
        return Results.success(hmeInputRecordService.inputRecordExport(tenantId, dto));
    }

    @ApiOperation(value = "投料报表异步导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/async-export")
    public ResponseEntity<?> asyncExport(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeInputRecordDTO dto) throws IOException {
        dto.initParam();
        hmeInputRecordService.asyncExport(tenantId, dto);
        return Results.success();
    }

    @ApiOperation(value = "创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      String fileName){
        HmeExportTaskVO exportTaskVO = hmeInputRecordService.createTask(tenantId, request, response, fileName);
        return Results.success(exportTaskVO);
    }
}
