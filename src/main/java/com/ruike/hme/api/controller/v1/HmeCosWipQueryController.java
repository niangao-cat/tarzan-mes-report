package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosWipQueryDTO;
import com.ruike.hme.app.service.HmeCosWipQueryService;
import com.ruike.hme.domain.vo.HmeCosWipQueryVO;
import com.ruike.hme.infra.mapper.HmeCosWipQueryMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController("HmeCosWipQueryController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-wip-query")
public class HmeCosWipQueryController {

    private final HmeCosWipQueryService hmeCosWipQueryService;
    private final HmeCosWipQueryMapper mapper;

    public HmeCosWipQueryController(HmeCosWipQueryService hmeCosWipQueryService, HmeCosWipQueryMapper mapper) {
        this.hmeCosWipQueryService = hmeCosWipQueryService;
        this.mapper = mapper;
    }

    @ApiOperation(value = "COS在制查询")
    @PostMapping(value = {"/query"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeCosWipQueryVO>> propertyCosWipQuery(@PathVariable("organizationId") Long tenantId,
                                                                      @RequestBody HmeCosWipQueryDTO dto,
                                                                      PageRequest pageRequest) {
        dto.validObject(tenantId, mapper);
        return Results.success(hmeCosWipQueryService.propertyCosWipQuery(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "COS在制查询")
    @GetMapping(value = {"/cos-wip-export"}, produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeCosWipQueryVO.class)
    public ResponseEntity<List<HmeCosWipQueryVO>> cosWipExport(@PathVariable("organizationId") Long tenantId,
                                                               HmeCosWipQueryDTO dto,
                                                               ExportParam exportParam,
                                                               HttpServletResponse response) {
        dto.validObject(tenantId, mapper);
        return Results.success(hmeCosWipQueryService.cosWipExport(tenantId, dto));
    }
}

