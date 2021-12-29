package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeEquipmentFaultMonitorDTO;
import com.ruike.hme.app.service.HmeEquipmentFaultMonitorService;
import com.ruike.hme.domain.vo.HmeEquipmentFaultMonitorVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.base.BaseController;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 设备故障监控
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/10 10:02
 */
@RestController("hmeEquipmentFaultMonitorController.v1")
@RequestMapping("/v1/{organizationId}/hme-equipment-fault-monitor")
@Api(SwaggerApiConfig.HME_EQUIPMENT_FAULT_MONITOR)
public class HmeEquipmentFaultMonitorController extends BaseController {

    private final HmeEquipmentFaultMonitorService hmeEquipmentFaultMonitorService;

    public HmeEquipmentFaultMonitorController(HmeEquipmentFaultMonitorService hmeEquipmentFaultMonitorService) {
        this.hmeEquipmentFaultMonitorService = hmeEquipmentFaultMonitorService;
    }

    @ApiOperation(value = "设备故障监控报表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeEquipmentFaultMonitorVO>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                                 HmeEquipmentFaultMonitorDTO dto,
                                                                 PageRequest pageRequest) {
        dto.initParam();
        Page<HmeEquipmentFaultMonitorVO> list = hmeEquipmentFaultMonitorService.pageList(pageRequest, dto, tenantId);
        return Results.success(list);
    }

    @ApiOperation(value = "设备故障监控报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeEquipmentFaultMonitorVO.class)
    public ResponseEntity<List<HmeEquipmentFaultMonitorVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                   ExportParam exportParam,
                                                                   HttpServletResponse response,
                                                                   HmeEquipmentFaultMonitorDTO dto) {
        dto.initParam();
        List<HmeEquipmentFaultMonitorVO> list = hmeEquipmentFaultMonitorService.export(dto, tenantId);
        return Results.success(list);
    }
}
