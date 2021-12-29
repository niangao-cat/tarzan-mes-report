package com.ruike.hme.api.controller.v1;

import com.ruike.hme.app.service.HmeMakeCenterProduceBoardService;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import io.choerodon.swagger.annotation.Permission;
import io.swagger.annotations.ApiOperation;
import org.hzero.core.util.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Objects;

/**
 * 制造中心生产看板
 *
 * @author sanfeng.zhang@hand-china.com 2021/5/28 17:24
 */
@RestController("HmeMakeCenterProduceBoardController.v1")
@RequestMapping("/v1/{organizationId}/hme-make-center-produce-board")
public class HmeMakeCenterProduceBoardController {

    @Autowired
    private HmeMakeCenterProduceBoardService hmeMakeCenterProduceBoardService;

    @ApiOperation(value = "日计划达成率")
    @GetMapping("/query-day-plan-reach-rate")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<HmeMakeCenterProduceBoardVO2>> queryDayPlanReachRateList(@PathVariable("organizationId") Long tenantId,
                                                                                        HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.initParam();
        return Results.success(hmeMakeCenterProduceBoardService.queryDayPlanReachRateList(tenantId, boardVO));
    }

    @ApiOperation(value = "月度计划")
    @GetMapping("/query-month-plan")
    @Permission(permissionPublic = true)
    public ResponseEntity<HmeMakeCenterProduceBoardVO5> queryMonthPlan(@PathVariable("organizationId") Long tenantId,
                                                                             HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.initParam();
        return Results.success(hmeMakeCenterProduceBoardService.queryMonthPlan(tenantId, boardVO));
    }

    @ApiOperation(value = "直通率明细")
    @GetMapping("/query-through-rate-details")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<HmeMakeCenterProduceBoardVO9>> queryThroughRateDetails(@PathVariable("organizationId") Long tenantId,
                                                                                      HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.initParam();
        return Results.success(hmeMakeCenterProduceBoardService.queryThroughRateDetails(tenantId, boardVO));
    }

    @ApiOperation(value = "工序不良top5")
    @GetMapping("/query-process-nc-top-five")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<HmeMakeCenterProduceBoardVO13>> queryProcessNcTopFive(@PathVariable("organizationId") Long tenantId,
                                                                                     HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.initParam();
        return Results.success(hmeMakeCenterProduceBoardService.queryProcessNcTopFive(tenantId, boardVO));
    }

    @ApiOperation(value = "巡检不良")
    @GetMapping("/query-inspection-nc")
    @Permission(permissionPublic = true)
    public ResponseEntity<HmeMakeCenterProduceBoardVO15> queryInspectionNc(@PathVariable("organizationId") Long tenantId,
                                                                                     HmeMakeCenterProduceBoardVO boardVO) {
        boardVO.initParam();
        return Results.success(hmeMakeCenterProduceBoardService.queryInspectionNc(tenantId, boardVO));
    }

    @ApiOperation(value = "月度计划-部门")
    @GetMapping("/query-area-month-plan")
    @Permission(permissionPublic = true)
    public ResponseEntity<HmeMakeCenterProduceBoardVO5> queryMonthPlanByArea(@PathVariable("organizationId") Long tenantId,
                                                                             HmeMakeCenterProduceBoardVO boardVO) {
        if(Objects.isNull(boardVO.getAreaCode())){
            throw new CommonException("制造部编码不能为空！");
        }
        HmeMakeCenterProduceBoardVO vo = hmeMakeCenterProduceBoardService.queryAreaIdByAreaCode(boardVO.getAreaCode());
        boardVO.setAreaId(vo.getAreaId());
        return Results.success(hmeMakeCenterProduceBoardService.queryMonthPlanByArea(tenantId, boardVO));
    }

    @ApiOperation(value = "直通率明细-部门")
    @GetMapping("/query-area-through-rate-details")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<HmeMakeCenterProduceBoardVO9>> queryThroughRateDetailsByArea(@PathVariable("organizationId") Long tenantId,
                                                                                            HmeMakeCenterProduceBoardVO boardVO) {
        if(Objects.isNull(boardVO.getAreaCode())){
            throw new CommonException("制造部编码不能为空！");
        }
        HmeMakeCenterProduceBoardVO vo = hmeMakeCenterProduceBoardService.queryAreaIdByAreaCode(boardVO.getAreaCode());
        boardVO.setAreaId(vo.getAreaId());
        return Results.success(hmeMakeCenterProduceBoardService.queryThroughRateDetailsByArea(tenantId, boardVO));
    }

    @ApiOperation(value = "不良top5-部门")
    @GetMapping("/query-material-nc-top-five")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<HmeMakeCenterProduceBoardVO13>> queryMaterialNcTopFive(@PathVariable("organizationId") Long tenantId,
                                                                                      HmeMakeCenterProduceBoardVO boardVO) {
        if(Objects.isNull(boardVO.getAreaCode())){
            throw new CommonException("制造部编码不能为空！");
        }
        HmeMakeCenterProduceBoardVO vo = hmeMakeCenterProduceBoardService.queryAreaIdByAreaCode(boardVO.getAreaCode());
        boardVO.setAreaId(vo.getAreaId());
        return Results.success(hmeMakeCenterProduceBoardService.queryMaterialNcTopFive(tenantId, boardVO));
    }


    @ApiOperation(value = "日计划达成率-部门")
    @GetMapping("/query-area-day-plan-reach-rate")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<HmeMakeCenterProduceBoardVO2>> queryDayPlanReachRateListByArea(@PathVariable("organizationId") Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        if(Objects.isNull(boardVO.getAreaCode())){
            throw new CommonException("制造部编码不能为空！");
        }
        HmeMakeCenterProduceBoardVO vo = hmeMakeCenterProduceBoardService.queryAreaIdByAreaCode(boardVO.getAreaCode());
        boardVO.setAreaId(vo.getAreaId());
        return Results.success(hmeMakeCenterProduceBoardService.queryDayPlanReachRateListByArea(tenantId, boardVO));
    }

    @ApiOperation(value = "查询看板制造部")
    @GetMapping("/query-kanban-area")
    @Permission(permissionPublic = true)
    public ResponseEntity<List<HmeMakeCenterProduceBoardVO18>> queryKanbanAreaList(@PathVariable("organizationId") Long tenantId) {
        return Results.success(hmeMakeCenterProduceBoardService.queryKanbanAreaList(tenantId));
    }

    @ApiOperation(value = "查询看板制造部")
    @GetMapping("/query-kanban-config")
    @Permission(permissionPublic = true)
    public ResponseEntity<HmeKanbanConfigVO> queryKanbanConfig(@PathVariable("organizationId") Long tenantId) {
        return Results.success(hmeMakeCenterProduceBoardService.queryKanbanConfig(tenantId));
    }

    @ApiOperation(value = "查询看板制造部")
    @GetMapping("/query-kanban-prod-line")
    @Permission(permissionPublic = true)
    public ResponseEntity<Page<HmeModProductionLineVO>> queryKanbanProdLine(@PathVariable("organizationId") Long tenantId,
                                                                            HmeModProductionLineVO lineVO,
                                                                            @ApiIgnore PageRequest pageRequest) {
        return Results.success(hmeMakeCenterProduceBoardService.queryKanbanProdLine(tenantId, lineVO, pageRequest));
    }


}
