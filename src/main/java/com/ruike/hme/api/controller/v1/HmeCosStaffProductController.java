package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeCosStaffProductRepository;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO;
import com.ruike.hme.domain.vo.HmeCosStaffProductVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * COS员工汇总报表
 *
 * @author sanfeng.zhang@hand-china.com
*/
@RestController("HmeCosStaffProductController.v1")
@RequestMapping("/v1/{organizationId}/hme-cos-staff-product")
public class HmeCosStaffProductController {

    @Autowired
    private HmeCosStaffProductRepository hmeCosStaffProductRepository;

    @ApiOperation(value = "COS员工汇总报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/staff-product-query")
    public ResponseEntity<Page<HmeCosStaffProductVO2>> staffProductQuery(@PathVariable("organizationId") Long tenantId,
                                                                         HmeCosStaffProductVO dto,
                                                                         @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        Page<HmeCosStaffProductVO2> list = hmeCosStaffProductRepository.staffProductQuery(tenantId, dto, pageRequest);
        return Results.success(list);
    }

    @ApiOperation(value = "COS员工汇总报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping(value = "/staff-product-export")
    @ExcelExport(HmeCosStaffProductVO2.class)
    public ResponseEntity<List<HmeCosStaffProductVO2>> staffProductExport(@PathVariable("organizationId") Long tenantId,
                                                                          HmeCosStaffProductVO dto,
                                                                          ExportParam exportParam,
                                                                          HttpServletResponse response) {
        dto.initParam();
        return Results.success(hmeCosStaffProductRepository.staffProductExport(tenantId, dto));
    }

}
