package com.ruike.hme.app.service.impl;

import com.ruike.hme.app.service.HmeMakeCenterProduceBoardService;
import com.ruike.hme.domain.repository.HmeMakeCenterProduceBoardRepository;
import com.ruike.hme.domain.vo.*;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日计划达成率
 *
 * @author sanfeng.zhang@hand-china.com 2021/5/28 18:14
 */
@Service
public class HmeMakeCenterProduceBoardServiceImpl implements HmeMakeCenterProduceBoardService {

    @Autowired
    private HmeMakeCenterProduceBoardRepository hmeMakeCenterProduceBoardRepository;

    @Override
    public List<HmeMakeCenterProduceBoardVO2> queryDayPlanReachRateList(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryDayPlanReachRateList(tenantId, boardVO);
    }

    @Override
    public HmeMakeCenterProduceBoardVO5 queryMonthPlan(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryMonthPlan(tenantId, boardVO);
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO9> queryThroughRateDetails(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryThroughRateDetails(tenantId, boardVO);
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO13> queryProcessNcTopFive(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryProcessNcTopFive(tenantId, boardVO);
    }

    @Override
    public HmeMakeCenterProduceBoardVO15 queryInspectionNc(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryInspectionNc(tenantId, boardVO);
    }

    @Override
    public HmeMakeCenterProduceBoardVO5 queryMonthPlanByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryMonthPlanByArea(tenantId, boardVO);
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO9> queryThroughRateDetailsByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryThroughRateDetailsByArea(tenantId, boardVO);
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO13> queryMaterialNcTopFive(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryMaterialNcTopFive(tenantId, boardVO);
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO2> queryDayPlanReachRateListByArea(Long tenantId, HmeMakeCenterProduceBoardVO boardVO) {
        return hmeMakeCenterProduceBoardRepository.queryDayPlanReachRateListByArea(tenantId, boardVO);
    }

    @Override
    public List<HmeMakeCenterProduceBoardVO18> queryKanbanAreaList(Long tenantId) {
        return hmeMakeCenterProduceBoardRepository.queryKanbanAreaList(tenantId);
    }

    @Override
    public HmeKanbanConfigVO queryKanbanConfig(Long tenantId) {
        return hmeMakeCenterProduceBoardRepository.queryKanbanConfig(tenantId);
    }

    @Override
    public Page<HmeModProductionLineVO> queryKanbanProdLine(Long tenantId, HmeModProductionLineVO lineVO, PageRequest pageRequest) {
        return hmeMakeCenterProduceBoardRepository.queryKanbanProdLine(tenantId, lineVO, pageRequest);
    }

    @Override
    public HmeMakeCenterProduceBoardVO queryAreaIdByAreaCode(String areaCode) {
        return hmeMakeCenterProduceBoardRepository.queryAreaIdByAreaCode(areaCode);
    }
}
