package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeStockInDetailsDTO;
import com.ruike.hme.app.service.HmeStockInDetailsService;
import com.ruike.hme.domain.vo.HmeStockInDetailsVO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 入库明细查询报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/03/04 11:35
 */
@RestController("hmeStockInDetailsController.v1")
@RequestMapping("/v1/{organizationId}/hme-stock-in-details")
public class HmeStockInDetailsController extends BaseController {

    @Autowired
    private HmeStockInDetailsService hmeStockInDetailsService;

    @ApiOperation(value = "入库明细查询报表列表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Page<HmeStockInDetailsVO>> queryList(@PathVariable("organizationId") Long tenantId,
                                                               @RequestBody HmeStockInDetailsDTO dto,
                                                               PageRequest pageRequest) {
        Page<HmeStockInDetailsVO> list = hmeStockInDetailsService.queryList(tenantId,dto,pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "入库明细查询报表导出")
    @GetMapping(value = "/list-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeStockInDetailsVO.class)
    public ResponseEntity<List<HmeStockInDetailsVO>> queryListExport(@PathVariable("organizationId") Long tenantId,
                                                          ExportParam exportParam,
                                                          HttpServletResponse response,
                                                          HmeStockInDetailsDTO condition) {
        return Results.success(hmeStockInDetailsService.queryListExport(tenantId, condition));
    }
}
