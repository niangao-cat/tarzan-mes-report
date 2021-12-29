package com.ruike.hme.api.controller.v1;

/**
 * @author sanfeng.zhang@hand-china.com 2021/4/14 17:02
 */

import com.ruike.hme.api.dto.HmeMaterielBadDetailedDTO;
import com.ruike.hme.domain.repository.HmeMaterielBadDetailedRepository;
import com.ruike.hme.domain.vo.HmeMaterielBadDetailedVO;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
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
 * 材料不良明细报表 API 管理
 *
 * @author 35113 2021/02/02 12:43
 */
@RestController("hmeMaterielBadDetailedController.v1")
@RequestMapping("/v1/{organizationId}/hme-material-bad-detailed")
public class HmeMaterielBadDetailedController {

    @Autowired
    private HmeMaterielBadDetailedRepository hmeMaterielBadDetailedRepository;

    @ApiOperation(value = "材料不良明细报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping
    @ProcessLovValue
    public ResponseEntity<Page<HmeMaterielBadDetailedVO>> list(@PathVariable("organizationId") Long tenantId,
                                                               HmeMaterielBadDetailedDTO dto,
                                                               @ApiIgnore PageRequest pageRequest) {
        dto.initParam();
        Page<HmeMaterielBadDetailedVO> list = hmeMaterielBadDetailedRepository.pageList(tenantId,dto,pageRequest);
        for(HmeMaterielBadDetailedVO vo : list){
            if(StringUtils.isBlank(vo.getFreezeFlag())){
                vo.setFreezeFlag("N");
            }
        }
        return Results.success(list);
    }

    @ApiOperation(value = "材料不良明细报表导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @GetMapping("/material-nc-export")
    @ProcessLovValue
    @ExcelExport(HmeMaterielBadDetailedVO.class)
    public ResponseEntity<List<HmeMaterielBadDetailedVO>> materialNcExport(@PathVariable("organizationId") Long tenantId,
                                                                           HmeMaterielBadDetailedDTO dto,
                                                                           ExportParam exportParam,
                                                                           HttpServletResponse response){
        dto.initParam();
        return Results.success(hmeMaterielBadDetailedRepository.materialNcExport(tenantId,dto));
    }
}

