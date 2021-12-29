package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeProLineDetailsWipService;
import com.ruike.hme.domain.vo.HmeProductEoInfoVO;
import com.ruike.hme.domain.vo.HmeProductionLineDetailsVO2;
import com.ruike.hme.domain.vo.HmeProductionQueryVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiModelProperty;
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

/**
 * 产线日明细报表 管理 API
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:40:20
 */

@RestController("hmeProLineDetailsController.v1")
@RequestMapping("/v1/{organizationId}/hme-pro-line-details")
public class HmeProLineDetailsWipController {

    @Autowired
    private HmeProLineDetailsWipService hmeProLineDetailsWipService;

    @ApiModelProperty(value = "在制查询报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/queryProductDetails")
    public ResponseEntity<HmeProductionLineDetailsVO2> queryProductDetails(@PathVariable("organizationId") Long tenantId,
                                                                           PageRequest pageRequest, HmeProductionQueryVO params){
        return Results.success(hmeProLineDetailsWipService.queryProductDetails(tenantId, pageRequest, params));
    }

    @ApiModelProperty(value = "在制报表-运行/库存EO信息")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/query-product-eo-list")
    public ResponseEntity<Page<HmeProductEoInfoVO>> queryProductEoList(@PathVariable("organizationId") Long tenantId,
                                                                          @ApiIgnore PageRequest pageRequest, HmeProductEoInfoVO params){
        return Results.success(hmeProLineDetailsWipService.queryProductEoList(tenantId, pageRequest, params));
    }

    @ApiOperation(value = "在制查询导出")
    @GetMapping(value = "/online-report-export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    public ResponseEntity<?> onlineReportExport(@PathVariable("organizationId") Long tenantId,
                                                HmeProductionQueryVO params, HttpServletResponse response) {
        try {
            hmeProLineDetailsWipService.onlineReportExport(tenantId, params, response);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
        return Results.success();
    }
}
