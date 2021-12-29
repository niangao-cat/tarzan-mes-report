package com.ruike.hme.api.controller.v1;

import com.ruike.hme.api.dto.HmeSelfRepairDTO;
import com.ruike.hme.domain.repository.HmeSelfRepairRepository;
import com.ruike.hme.domain.vo.HmeSelfRepairVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * 自制件返修统计报表
 *
 * @author xin.t@raycuslaser 2021/7/5 15:17
 */
@RestController("HmeSelfRepairController.v1")
@RequestMapping("/v1/{organizationId}/hme-self-repair")
@Api(tags = SwaggerApiConfig.HME_SELF_REPAIR_REPORT)
public class HmeSelfRepairController {

    @Autowired
    private HmeSelfRepairRepository hmeSelfRepairRepository;

    @ApiOperation(value = "自制件返修统计报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/query")
    public ResponseEntity<Page<HmeSelfRepairVO>> query(@PathVariable(value = "organizationId") Long tenantId,
                                                       HmeSelfRepairDTO dto,
                                                       @ApiIgnore PageRequest pageRequest){
        dto.init();
        return Results.success(hmeSelfRepairRepository.query(tenantId,dto,pageRequest));
    }

    @ApiOperation(value = "自制件返修统计报表-导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/export",produces = "application/json;charset=UTF-8")
    @ExcelExport(HmeSelfRepairVO.class)
    public ResponseEntity<List<HmeSelfRepairVO>> export(@PathVariable(value = "organizationId") Long tenantId,
                                                         HmeSelfRepairDTO dto,
                                                         HttpServletResponse response,
                                                         ExportParam exportParam){
        dto.init();
        return Results.success(hmeSelfRepairRepository.export(tenantId,dto));
    }
}
