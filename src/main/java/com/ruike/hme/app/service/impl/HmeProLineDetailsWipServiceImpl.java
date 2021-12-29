package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeProductionQueryDTO;
import com.ruike.hme.app.service.HmeProLineDetailsWipService;
import com.ruike.hme.domain.repository.HmeProLineDetailsWipRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.HmeProLineDetailsWipMapper;
import com.ruike.qms.infra.util.CollectorsUtil;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 产线日明细报表业务实现
 *
 * @author bao.xu@hand-china.com 2020-07-07 11:10:58
 */

@Service
public class HmeProLineDetailsWipServiceImpl implements HmeProLineDetailsWipService {

    @Autowired
    private HmeProLineDetailsWipMapper hmeProLineDetailsMapper;

    @Autowired
    private HmeProLineDetailsWipRepository hmeProLineDetailsWipRepository;

    @Override
    public HmeProductionLineDetailsVO2 queryProductDetails(Long tenantId, PageRequest pageRequest, HmeProductionQueryVO params) {
        HmeProductionLineDetailsVO2 result = new HmeProductionLineDetailsVO2();
        params.setTenantId(tenantId);
        Page<HmeProductionQueryDTO> resultList = PageHelper.doPageAndSort(pageRequest, () -> hmeProLineDetailsWipRepository.queryProductDetails(params));
        result.setPageData(resultList);
        if (resultList.size() == 0) {
            return result;
        }

        // 查询产线下汇总工序的数量
        List<String> materialIdList = resultList.getContent().stream().map(HmeProductionQueryDTO::getMaterialId).filter(Objects::nonNull).collect(Collectors.toList());
        List<HmeProductDetailsVO> hmeProductDetailsVOList = hmeProLineDetailsWipRepository.batchQueryWorkingQTYAndCompletedQTY(tenantId, params.getSiteId(), params.getProdLineId(), materialIdList);
        Map<String, String> workcellMap = hmeProductDetailsVOList.stream().collect(Collectors.toMap(HmeProductDetailsVO::getWorkcellId, HmeProductDetailsVO::getDescription, (k1, k2) -> k1, LinkedMap::new));


        // 按物料和工段汇总数量
        Map<String, List<HmeProductDetailsVO>> qtyMap = hmeProductDetailsVOList.stream().collect(Collectors.groupingBy(dto -> dto.getMaterialId() + "_" + dto.getWorkcellId()));

        Map<String, BigDecimal> queueNumMap = hmeProLineDetailsWipRepository.selectQueueNumByMaterialList(tenantId, params.getProdLineId(), params.getSiteId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));
        Map<String, BigDecimal> unCountMap = hmeProLineDetailsWipRepository.selectUnCountByMaterialList(tenantId, params.getProdLineId(), materialIdList).stream().collect(Collectors.toMap(HmeEoVO::getMaterialId, HmeEoVO::getQty, (k1, k2) -> k1));
        for (HmeProductionQueryDTO dto : resultList.getContent()) {
            List<HmeProcessInfoVO> resultMap = new ArrayList<>();
            if (!workcellMap.isEmpty()) {
                workcellMap.forEach((workcellId, description) -> {
                    HmeProcessInfoVO process = new HmeProcessInfoVO();
                    // 从map中取出数量，若没有则为0
                    String key = dto.getMaterialId() + "_" + workcellId;
                    BigDecimal runNum = BigDecimal.ZERO, finishNum = BigDecimal.ZERO;
                    if (qtyMap.containsKey(key)) {
                        List<HmeProductDetailsVO> qtyList = qtyMap.get(key);
                        runNum = qtyList.stream().map(HmeProductDetailsVO::getRunNum).reduce(BigDecimal.ZERO, BigDecimal::add);
                        finishNum = qtyList.stream().map(HmeProductDetailsVO::getFinishNum).reduce(BigDecimal.ZERO, BigDecimal::add);
                    }
                    // 组装数据
                    process.setMaterialId(dto.getMaterialId());
                    process.setWorkcellId(workcellId);
                    process.setDescription(workcellMap.get(workcellId));
                    process.setRunNum(runNum);
                    process.setFinishNum(finishNum);
                    resultMap.add(process);
                });
            }
            dto.setWorkcells(resultMap);
            dto.setQueueNum(queueNumMap.containsKey(dto.getMaterialId()) ? queueNumMap.get(dto.getMaterialId()).longValue() : 0);
            dto.setUnCount(unCountMap.containsKey(dto.getMaterialId()) ? unCountMap.get(dto.getMaterialId()).longValue() : 0);
        }
        //工序在制合计查询
        List<HmeProductionLineDetailsVO3> hmeProductionLineDetailsVO3s = hmeProLineDetailsMapper.processQtyQuery(tenantId, params.getSiteId(), params.getProdLineId(), params.getMaterialId());
        result.setWorkcellQty(hmeProductionLineDetailsVO3s);
        BigDecimal qty = BigDecimal.ZERO;
        if(CollectionUtils.isNotEmpty(hmeProductionLineDetailsVO3s)){
            qty = hmeProductionLineDetailsVO3s.stream().collect(CollectorsUtil
                    .summingBigDecimal(item -> item.getTotalQty()));
        }
        result.setQty(qty);
        return result;
    }

    @Override
    public Page<HmeProductEoInfoVO> queryProductEoList(Long tenantId, PageRequest pageRequest, HmeProductEoInfoVO params) {
        return hmeProLineDetailsWipRepository.queryProductEoList(tenantId, pageRequest, params);
    }

    @Override
    public void onlineReportExport(Long tenantId, HmeProductionQueryVO params, HttpServletResponse response) throws IOException {
        hmeProLineDetailsWipRepository.onlineReportExport(tenantId, params, response);
    }
}
