package com.ruike.wms.api.controller.v1;

import com.ruike.wms.api.dto.WmsFinishWarehouseDTO;
import com.ruike.wms.domain.repository.WmsFinishWarehouseRepository;
import com.ruike.wms.domain.vo.WmsFinishWarehouseVO2;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 完工及入库数量汇总报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/5/21 15:24
 */
@RestController("WmsFinishWarehouseController.v1")
@RequestMapping("/v1/{organizationId}/finish-warehouse")
public class WmsFinishWarehouseController {

    @Autowired
    private WmsFinishWarehouseRepository wmsFinishWarehouseRepository;


    @ApiOperation(value = "完工及入库数量汇总报表 查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query-summary")
    public ResponseEntity<WmsFinishWarehouseVO2> querySummary(@PathVariable("organizationId") Long tenantId,
                                                              WmsFinishWarehouseDTO dto,
                                                              @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        return Results.success(wmsFinishWarehouseRepository.querySummary(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "完工及入库数量汇总报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    public ResponseEntity<?> export(@PathVariable("organizationId") Long tenantId,
                                    WmsFinishWarehouseDTO dto,
                                    HttpServletResponse response) {
        dto.initParam();
        try {
            wmsFinishWarehouseRepository.export(tenantId, dto, response);
        }catch (IOException e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }
}
