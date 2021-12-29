package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeAfterSalesRegisterDTO;
import com.ruike.hme.app.service.HmeAfterSalesRegisterService;
import com.ruike.hme.domain.vo.HmeAfterSalesRegisterVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName HmeAfterSalesRegisterController
 * @Description 售后登记查询报表
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/17 10:58
 * @Version 1.0
 **/
@RestController("HmeAfterSalesRegisterController.v1")
@RequestMapping("/v1/{organizationId}/Hme-after-sales-register")
public class HmeAfterSalesRegisterController extends BaseController {

    private final HmeAfterSalesRegisterService hmeAfterSalesRegisterService;

    public HmeAfterSalesRegisterController(HmeAfterSalesRegisterService hmeAfterSalesRegisterService) {
        this.hmeAfterSalesRegisterService = hmeAfterSalesRegisterService;
    }

    @ApiOperation(value = "售后登记查询报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query")
    public ResponseEntity<Page<HmeAfterSalesRegisterVO>> query(@PathVariable(value = "organizationId") Long tenantId,
                                                               PageRequest pageRequest,
                                                               HmeAfterSalesRegisterDTO dto) {
        Page<HmeAfterSalesRegisterVO> hmeAfterSalesRegisterVO = hmeAfterSalesRegisterService.query(tenantId, dto, pageRequest);
        return Results.success(hmeAfterSalesRegisterVO);
    }

    @ApiOperation(value = "售后登记查询报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeAfterSalesRegisterVO.class)
    public ResponseEntity<List<HmeAfterSalesRegisterVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                ExportParam exportParam,
                                                                HttpServletResponse response,
                                                                HmeAfterSalesRegisterDTO dto) {
        List<HmeAfterSalesRegisterVO> result = hmeAfterSalesRegisterService.export(tenantId, dto);
        return Results.success(result);
    }

}
