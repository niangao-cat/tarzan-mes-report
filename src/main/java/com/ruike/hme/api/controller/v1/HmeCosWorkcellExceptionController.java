package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.HmeCosWorkcellExceptionDTO;
import com.ruike.hme.app.service.HmeCosWorkcellExceptionService;
import com.ruike.hme.domain.vo.HmeCosWorkcellExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * <p>
 * COS工位加工异常汇总表 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/13 11:01
 */
@Slf4j
@RestController("hmeCosWorkcellExceptionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-workcell-exception")
@Api(tags = SwaggerApiConfig.HME_COS_WORKCELL_EXCEPTION)
public class HmeCosWorkcellExceptionController extends BaseController {

    private final HmeCosWorkcellExceptionService hmeCosWorkcellExceptionService;

    public HmeCosWorkcellExceptionController(HmeCosWorkcellExceptionService hmeCosWorkcellExceptionService) {
        this.hmeCosWorkcellExceptionService = hmeCosWorkcellExceptionService;
    }

    @ApiOperation(value = "COS工位加工异常汇总表 分页查询")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeCosWorkcellExceptionVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                     HmeCosWorkcellExceptionDTO dto,
                                                                     PageRequest pageRequest) {
        dto.initParam();
        return Results.success(hmeCosWorkcellExceptionService.queryList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "COS工位加工异常汇总表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/export")
    @ExcelExport(HmeCosWorkcellExceptionVO.class)
    public ResponseEntity<List<HmeCosWorkcellExceptionVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                                  ExportParam exportParam,
                                                                  HttpServletResponse response,
                                                                  HmeCosWorkcellExceptionDTO dto) {
        dto.initParam();
        return Results.success(hmeCosWorkcellExceptionService.export(tenantId, dto));
    }

}