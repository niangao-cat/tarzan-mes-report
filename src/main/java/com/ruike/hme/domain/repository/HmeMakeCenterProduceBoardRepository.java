package com.ruike.hme.domain.repository;

import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;

import java.util.List;

/**
 * 日计划达成率
 *
 * @author sanfeng.zhang@hand-china.com 2021/5/28 18:15
 */
public interface HmeMakeCenterProduceBoardRepository {

    /**
     * 日计划达成率
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     * @author sanfeng.zhang@hand-china.com 2021/5/28
     */
    List<HmeMakeCenterProduceBoardVO2> queryDayPlanReachRateList(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 月度计划
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO5>
     * @author sanfeng.zhang@hand-china.com 2021/5/31
     */
    HmeMakeCenterProduceBoardVO5 queryMonthPlan(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 直通率明细
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO9>
     * @author sanfeng.zhang@hand-china.com 2021/6/1
     */
    List<HmeMakeCenterProduceBoardVO9> queryThroughRateDetails(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 工序不良top5
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO13>
     * @author sanfeng.zhang@hand-china.com 2021/6/2
     */
    List<HmeMakeCenterProduceBoardVO13> queryProcessNcTopFive(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 巡检不良
     * @param tenantId
     * @param boardVO
     * @return com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO15
     * @author sanfeng.zhang@hand-china.com 2021/6/3
     */
    HmeMakeCenterProduceBoardVO15 queryInspectionNc(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 月度计划-部门
     *
     * @param tenantId
     * @param boardVO
     * @return com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO5
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    HmeMakeCenterProduceBoardVO5 queryMonthPlanByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 直通率-部门
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO9>
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    List<HmeMakeCenterProduceBoardVO9> queryThroughRateDetailsByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 不良top5-部门
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO13>
     * @author sanfeng.zhang@hand-china.com 2021/6/7
     */
    List<HmeMakeCenterProduceBoardVO13> queryMaterialNcTopFive(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 日计划达成率-部门
     *
     * @param tenantId
     * @param boardVO
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO2>
     * @author sanfeng.zhang@hand-china.com 2021/6/8
     */
    List<HmeMakeCenterProduceBoardVO2> queryDayPlanReachRateListByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO);

    /**
     * 查询看板制造部
     * @param tenantId
     * @return java.util.List<com.ruike.hme.domain.vo.HmeMakeCenterProduceBoardVO18>
     * @author sanfeng.zhang@hand-china.com 2021/6/8
     */
    List<HmeMakeCenterProduceBoardVO18> queryKanbanAreaList(Long tenantId);

    /**
     * 查询看板配置
     * @param tenantId
     * @return com.ruike.hme.domain.vo.HmeKanbanConfigVO
     * @author sanfeng.zhang@hand-china.com 2021/8/5
     */
    HmeKanbanConfigVO queryKanbanConfig(Long tenantId);

    /**
     * 查询产线
     * @param tenantId
     * @param lineVO
     * @param pageRequest
     * @return io.choerodon.core.domain.Page<com.ruike.hme.domain.vo.HmeModProductionLineVO>
     * @author sanfeng.zhang@hand-china.com 2021/8/5
     */
    Page<HmeModProductionLineVO> queryKanbanProdLine(Long tenantId, HmeModProductionLineVO lineVO, PageRequest pageRequest);

    HmeMakeCenterProduceBoardVO queryAreaIdByAreaCode(String areaCode);
}
