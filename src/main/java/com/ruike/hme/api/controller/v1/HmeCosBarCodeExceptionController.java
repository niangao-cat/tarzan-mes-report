package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeCosBarCodeExceptionDTO;
import com.ruike.hme.app.service.HmeCosBarCodeExceptionService;
import com.ruike.hme.domain.vo.HmeCosBarCodeExceptionVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.util.Results;
import org.hzero.export.annotation.ExcelExport;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * COS条码加工异常汇总报表
 *
 * @author JUNFENG.CHEN@HAND-CHINA.COM 2021/01/26 15:26
 */
@Slf4j
@RestController("hmeCosBarCodeExceptionController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-barcode-exception")
public class HmeCosBarCodeExceptionController {

    @Autowired
    private HmeCosBarCodeExceptionService hmeCosBarCodeExceptionService;

    @ApiOperation(value = "表查询逻辑")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value="/list")
    public ResponseEntity<Page<HmeCosBarCodeExceptionVO>> queryList(@PathVariable(value = "organizationId") Long tenantId,
                                                                    HmeCosBarCodeExceptionDTO dto, PageRequest pageRequest) {
        dto.initParam();
        return Results.success(hmeCosBarCodeExceptionService.queryList(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "COS条码加工异常汇总报表导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @ExcelExport(HmeCosBarCodeExceptionVO.class)
    public ResponseEntity<List<HmeCosBarCodeExceptionVO>> listForExport(@PathVariable("organizationId") Long tenantId,
                                                                        ExportParam exportParam,
                                                                        HttpServletResponse response,
                                                                        HmeCosBarCodeExceptionDTO condition) {
        condition.initParam();
        return Results.success(hmeCosBarCodeExceptionService.listForExport(tenantId, condition));
    }

}
