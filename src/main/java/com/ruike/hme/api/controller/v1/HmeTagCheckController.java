package com.ruike.hme.api.controller.v1;

import com.ruike.hme.domain.repository.HmeTagCheckRepository;
import com.ruike.hme.domain.vo.HmeExportTaskVO;
import com.ruike.hme.domain.vo.HmeTagCheckQueryVO;
import com.ruike.hme.domain.vo.HmeTagCheckVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.core.iam.ResourceLevel;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 数据项展示报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/9/1 11:36
 */
@RestController("hmeTagCheckController.v1")
@RequestMapping("/v1/{organizationId}/hme-tag-checks")
public class HmeTagCheckController {

    @Autowired
    private HmeTagCheckRepository hmeTagCheckRepository;

    @ApiOperation(value = "数据项展示报表")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/list")
    public ResponseEntity<Page<HmeTagCheckVO2>> queryList(@PathVariable("organizationId") Long tenantId,
                                                          @RequestBody HmeTagCheckQueryVO queryVO,
                                                          PageRequest pageRequest) {
        queryVO.initParam();
        return Results.success(hmeTagCheckRepository.queryList(tenantId, queryVO, pageRequest));
    }

    @ApiOperation(value = "数据项展示报表 导出")
    @Permission(level = ResourceLevel.ORGANIZATION)
    @PostMapping("/async-export")
    public ResponseEntity<?> asyncExport(@PathVariable("organizationId") Long tenantId,
                                         @RequestBody HmeTagCheckQueryVO queryVO) throws IOException {
        queryVO.initParam();
        hmeTagCheckRepository.asyncExport(tenantId, queryVO);
        return Results.success();
    }

    @ApiOperation(value = "创建任务")
    @PostMapping("/create-task")
    @Permission(level = ResourceLevel.ORGANIZATION)
    public ResponseEntity<HmeExportTaskVO> createTask(@PathVariable("organizationId") Long tenantId,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response){
        return Results.success(hmeTagCheckRepository.createTask(tenantId, request, response));
    }
}
