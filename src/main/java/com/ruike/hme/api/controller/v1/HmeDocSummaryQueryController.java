package com.ruike.hme.api.controller.v1;


import com.ruike.hme.api.dto.HmeDocSummaryQueryDTO;
import com.ruike.hme.domain.repository.HmeDocSummaryQueryRepository;
import com.ruike.hme.domain.vo.HmeDocSummaryQueryVO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import tarzan.config.SwaggerApiConfig;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName HmeDocSummaryQueryController
 * @Description 单据汇总查询报表
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/2/25 16:22
 * @Version 1.0
 **/
@RestController("hmeDocSummaryQueryController.v1")
@RequestMapping("/v1/{organizationId}/doc-summary")
public class HmeDocSummaryQueryController extends BaseController {

    @Autowired
    private HmeDocSummaryQueryRepository hmeDocSummaryQueryRepository;

    @ApiOperation(value = "单据汇总查询报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    public ResponseEntity<Page<HmeDocSummaryQueryVO>> list(@PathVariable("organizationId") Long tenantId,
                                                           HmeDocSummaryQueryDTO dto,
                                                           @ApiIgnore PageRequest pageRequest) {
        Page<HmeDocSummaryQueryVO> list = hmeDocSummaryQueryRepository.pageList(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "单据汇总查询报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/report-export")
    @ExcelExport(HmeDocSummaryQueryVO.class)
    public ResponseEntity<List<HmeDocSummaryQueryVO>> reportExport(@PathVariable("organizationId") Long tenantId,
                                                                   HmeDocSummaryQueryDTO dto,
                                                                   ExportParam exportParam,
                                                                   HttpServletResponse response) {
        return Results.success(hmeDocSummaryQueryRepository.reportExport(tenantId, dto));
    }


}