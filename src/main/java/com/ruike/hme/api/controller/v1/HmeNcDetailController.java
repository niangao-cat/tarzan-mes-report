package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.query.HmeNcDetailQuery;
import com.ruike.hme.domain.repository.HmeNcDetailRepository;
import com.ruike.hme.domain.vo.HmeNcDetailVO;
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
 * <p>
 * 工序不良明细 API管理
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/14 16:27
 */
@RestController("hmeNcDetailController.v1")
@RequestMapping("/v1/{organizationId}/hme-nc-detail")
@Api(tags = SwaggerApiConfig.HME_NC_DETAIL)
public class HmeNcDetailController extends BaseController {

    private final HmeNcDetailRepository repository;

    public HmeNcDetailController(HmeNcDetailRepository repository) {
        this.repository = repository;
    }

    @ApiOperation("工序不良明细 分页查询")
    @GetMapping
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<Page<HmeNcDetailVO>> list(@PathVariable(value = "organizationId") Long tenantId,
                                                    HmeNcDetailQuery dto,
                                                    PageRequest pageRequest) {
        this.validObject(dto);
        dto.validParam();
        return Results.success(repository.pagedList(tenantId, dto, pageRequest));
    }

    @ApiOperation(value = "工序不良明细 导出")
    @GetMapping(value = "/export", produces = "application/json;charset=UTF-8")
    @Permission(level = ResourceLevel.ORGANIZATION, permissionLogin = true)
    @ExcelExport(HmeNcDetailVO.class)
    public ResponseEntity<List<HmeNcDetailVO>> export(@PathVariable("organizationId") Long tenantId,
                                                      HmeNcDetailQuery dto,
                                                      HttpServletResponse response,
                                                      ExportParam exportParam) {
        this.validObject(dto);
        dto.validParam();
        List<HmeNcDetailVO> list = repository.export(tenantId, dto);
        return Results.success(list);
    }
}
