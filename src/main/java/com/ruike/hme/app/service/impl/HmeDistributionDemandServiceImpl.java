package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeDistributionDemandQueryDTO;
import com.ruike.hme.app.service.HmeDistributionDemandService;
import com.ruike.hme.domain.vo.HmeDistributionDemandQtyVO;
import com.ruike.hme.domain.vo.HmeDistributionDemandRepresentationVO;
import com.ruike.hme.domain.vo.HmeDistributionDemandVO;
import com.ruike.hme.infra.mapper.HmeDistributionDemandMapper;
import com.ruike.hme.infra.util.CollectorsUtils;
import com.ruike.hme.infra.util.DatetimeUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.export.vo.ExportParam;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 配送需求滚动报表 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/5 10:50
 */
@Service
public class HmeDistributionDemandServiceImpl implements HmeDistributionDemandService {

    private final HmeDistributionDemandMapper mapper;

    public HmeDistributionDemandServiceImpl(HmeDistributionDemandMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Page<HmeDistributionDemandRepresentationVO> page(Long tenantId, PageRequest pageRequest, HmeDistributionDemandQueryDTO query) {
        List<HmeDistributionDemandVO> demandList = mapper.selectDemandList(tenantId, query);
        if (CollectionUtils.isEmpty(demandList)) {
            return new Page<>();
        }
        Map<HmeDistributionDemandRepresentationVO, List<HmeDistributionDemandVO>> demandMap = demandList.stream().collect(Collectors.groupingBy(HmeDistributionDemandVO::toRepresentation));
        Page<HmeDistributionDemandRepresentationVO> page = Utils.pagedList(pageRequest.getPage(), pageRequest.getSize(), new ArrayList<>(demandMap.keySet()));
        displayFieldsCompletion(tenantId, query, demandMap, page.getContent());
        return page;
    }

    @Override
    public List<HmeDistributionDemandRepresentationVO> export(Long tenantId, HmeDistributionDemandQueryDTO query, ExportParam exportParam) {
        List<HmeDistributionDemandVO> demandList = mapper.selectDemandList(tenantId, query);
        if (CollectionUtils.isEmpty(demandList)) {
            return new ArrayList<>();
        }
        Map<HmeDistributionDemandRepresentationVO, List<HmeDistributionDemandVO>> demandMap = demandList.stream().collect(Collectors.groupingBy(HmeDistributionDemandVO::toRepresentation));
        List<HmeDistributionDemandRepresentationVO> list = new ArrayList<>(demandMap.keySet());
        displayFieldsCompletion(tenantId, query, demandMap, list);
        return list;
    }

    private void displayFieldsCompletion(Long tenantId, HmeDistributionDemandQueryDTO query, Map<HmeDistributionDemandRepresentationVO, List<HmeDistributionDemandVO>> demandMap, List<HmeDistributionDemandRepresentationVO> list) {
        // 计算线边库存和库存量
        List<HmeDistributionDemandQtyVO> workcellQtyList = mapper.selectWorkcellQtyList(tenantId, list.stream().map(r -> new HmeDistributionDemandQtyVO(r.getMaterialId(), r.getMaterialVersion(), r.getSiteId(), r.getWorkcellId())).collect(Collectors.toSet()));
        Map<String, BigDecimal> workcellQtyMap = workcellQtyList.stream().collect(Collectors.groupingBy(r -> r.getMaterialId() + "," + r.getMaterialVersion() + "," + r.getWorkcellId(), CollectorsUtils.summingBigDecimal(HmeDistributionDemandQtyVO::getQuantity)));
        List<HmeDistributionDemandQtyVO> inventoryQtyList = mapper.selectInventoryQtyList(tenantId, list.stream().map(r -> new HmeDistributionDemandQtyVO(r.getMaterialId(), r.getMaterialVersion(), r.getSiteId())).collect(Collectors.toSet()));
        Map<String, BigDecimal> inventoryQtyMap = inventoryQtyList.stream().collect(Collectors.groupingBy(r -> r.getMaterialId() + "," + r.getMaterialVersion(), CollectorsUtils.summingBigDecimal(HmeDistributionDemandQtyVO::getQuantity)));

        AtomicInteger seqGen = new AtomicInteger(0);
        BigDecimal oneDayHours = BigDecimal.valueOf(24);
        Date nowDate = DatetimeUtils.getBeginOfDate(new Date());
        list.forEach(rec -> {
            List<HmeDistributionDemandVO> demands = demandMap.get(rec);
            List<HmeDistributionDemandVO> currentDemands = demands.stream().filter(d -> d.getDemandDate().equals(query.getStartDate())).collect(Collectors.toList());
            BigDecimal currentDemandQty = currentDemands.stream().map(HmeDistributionDemandVO::getDemandQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal futureDemandQty = demands.stream().filter(d -> d.getDemandDate().compareTo(query.getStartDate()) != 0).map(HmeDistributionDemandVO::getDemandQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            rec.setSequence(seqGen.incrementAndGet());
            rec.setNowDate(nowDate);
            rec.setCurrentDemandQty(currentDemandQty);
            rec.setDemandGapQty(currentDemandQty.subtract(rec.getDistributedQty()));
            rec.setWorkcellQty(workcellQtyMap.getOrDefault(rec.getMaterialId() + "," + rec.getMaterialVersion() + "," + rec.getWorkcellId(), BigDecimal.ZERO));
            rec.setEstimatedStopTime(BigDecimal.ZERO.equals(currentDemandQty) ? BigDecimal.ZERO : (rec.getDistributedQty().compareTo(currentDemandQty) >= 0 ? BigDecimal.ONE : rec.getDistributedQty().divide(currentDemandQty, 6, BigDecimal.ROUND_HALF_UP)).multiply(oneDayHours).setScale(1, BigDecimal.ROUND_HALF_UP));
            rec.setFutureDemandQty(futureDemandQty);
            rec.setInventoryQty(inventoryQtyMap.getOrDefault(rec.getMaterialId() + "," + rec.getMaterialVersion(), BigDecimal.ZERO));
            rec.setInventoryGapQty(futureDemandQty.subtract(rec.getInventoryQty()));
        });
    }
}
