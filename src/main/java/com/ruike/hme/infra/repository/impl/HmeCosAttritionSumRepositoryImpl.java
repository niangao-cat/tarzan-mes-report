package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.api.dto.HmeCosAttritionSumDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumRepresentationDTO;
import com.ruike.hme.domain.repository.HmeCosAttritionSumRepository;
import com.ruike.hme.domain.vo.BomComponentWorkcellVO;
import com.ruike.hme.domain.vo.HmeCosAttritionSumVO;
import com.ruike.hme.domain.vo.WorkcellVO;
import com.ruike.hme.infra.mapper.HmeCommonReportMapper;
import com.ruike.hme.infra.mapper.HmeCosAttritionSumMapper;
import com.ruike.hme.infra.mapper.WorkOrderAttritionSumMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.StringCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;
import utils.Utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.Constant.ConstantValue.*;

/**
 * @author sanfeng.zhang@hand-china.com 2021/7/30 10:26
 */
@Component
public class HmeCosAttritionSumRepositoryImpl implements HmeCosAttritionSumRepository {

    @Autowired
    private HmeCosAttritionSumMapper hmeCosAttritionSumMapper;
    @Autowired
    private WorkOrderAttritionSumMapper workOrderAttritionSumMapper;
    @Autowired
    private HmeCommonReportMapper hmeCommonReportMapper;

    private final static BigDecimal PERCENT = new BigDecimal("100");

    @Override
    @ProcessLovValue
    public Page<HmeCosAttritionSumDTO> page(Long tenantId, WorkOrderAttritionSumQueryDTO query, PageRequest pageRequest) {
        List<HmeCosAttritionSumDTO> list = hmeCosAttritionSumMapper.selectList(tenantId, query);
        if (CollectionUtils.isNotEmpty(list)) {
            list = representationComplete(tenantId, list, query);
        }
        Page<HmeCosAttritionSumDTO> page = Utils.pagedList(pageRequest.getPage(), pageRequest.getSize(), list);
        return page;
    }

    @Override
    @ProcessLovValue
    public List<HmeCosAttritionSumDTO> export(Long tenantId, WorkOrderAttritionSumQueryDTO query) {
        List<HmeCosAttritionSumDTO> list = hmeCosAttritionSumMapper.selectList(tenantId, query);
        if (CollectionUtils.isNotEmpty(list)) {
            list = representationComplete(tenantId, list, query);
        }
        return list;
    }

    private Map<String, BigDecimal> processNcScrapGet(Long tenantId, List<HmeCosAttritionSumDTO> list, WorkOrderAttritionSumQueryDTO dto) {
        String defaultSiteId = hmeCommonReportMapper.getSiteIdByUserId(DetailsHelper.getUserDetails().getUserId());
        Map<String, BigDecimal> assembleQtyMap = new HashMap<>();
        Map<String, BigDecimal> ncQtyMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(list)) {
            List<List<HmeCosAttritionSumDTO>> splitSqlList = CommonUtils.splitSqlList(list, 2000);
            List<HmeCosAttritionSumVO> cosNcQtyList = new ArrayList<>();
            List<HmeCosAttritionSumVO> cosAttritionSumVOList = new ArrayList<>();
            for (List<HmeCosAttritionSumDTO> doList : splitSqlList) {
//                List<HmeCosAttritionSumVO> ncScrapList = hmeCosAttritionSumMapper.processNcScrapGet(tenantId, doList, defaultSiteId);
                List<HmeCosAttritionSumVO> ncScrapList =  hmeCosAttritionSumMapper.processNcScrapGet2(tenantId, doList);
                if (CollectionUtils.isNotEmpty(ncScrapList)) {
                    cosAttritionSumVOList.addAll(ncScrapList);
                }
                List<HmeCosAttritionSumVO> ncQuantitys = hmeCosAttritionSumMapper.queryNcQuantity(tenantId, doList, defaultSiteId);
                if (CollectionUtils.isNotEmpty(ncQuantitys)) {
                    cosNcQtyList.addAll(ncQuantitys);
                }
            }
            assembleQtyMap = cosAttritionSumVOList.stream().collect(Collectors.toMap(vo -> this.spliceStr2(vo), HmeCosAttritionSumVO::getDefectCount, (n1,n2) -> n1.add(n2)));
            // 根据loadSequence去重 再汇总数量
            List<HmeCosAttritionSumVO> distinctCosNcQtyList = cosNcQtyList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(s -> s.getLoadSequence()))), ArrayList::new));
            ncQtyMap = distinctCosNcQtyList.stream().collect(Collectors.toMap(vo -> this.spliceStr(vo), HmeCosAttritionSumVO::getDefectCount, (n1,n2) -> n1.add(n2)));
        }
        for (HmeCosAttritionSumDTO vo : list) {
            StringBuffer sb = new StringBuffer();
            sb.append(vo.getWorkOrderId());
            sb.append(vo.getComponentMaterialId());
            BigDecimal processNcScrapQty = assembleQtyMap.get(sb.toString());
            vo.setProcessNcScrapQty(processNcScrapQty != null ? processNcScrapQty : BigDecimal.ZERO);

            sb.append(vo.getWorkcellId());
            BigDecimal ncQty = ncQtyMap.get(sb.toString());
            vo.setPendingNcQuantity(ncQty != null ? ncQty : BigDecimal.ZERO);
        }
        return assembleQtyMap;
    }

    private String spliceStr(HmeCosAttritionSumVO vo) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getWorkOrderId());
        sb.append(vo.getMaterialId());
        sb.append(vo.getLineWorkcellId());
        return sb.toString();
    }

    private String spliceStr2(HmeCosAttritionSumVO vo) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getWorkOrderId());
        sb.append(vo.getMaterialId());
        return sb.toString();
    }

    private List<HmeCosAttritionSumDTO> representationComplete (Long tenantId, List<HmeCosAttritionSumDTO>  list,  WorkOrderAttritionSumQueryDTO dto) {
        // 获取工段相关
        list = this.workcellRelationGet(tenantId, list, dto);

        // 工序不良报废
        Map<String, BigDecimal> assembleQtyMap = this.processNcScrapGet(tenantId, list, dto);

        // 整合替代料
        list = this.substituteMaterialGet(tenantId, list, assembleQtyMap);

        // 根据结果筛选
        list = list.stream().filter(rec -> (StringUtils.isBlank(dto.getScrappedOverFlag()) || StringCommonUtils.equalsIgnoreBlank(dto.getScrappedOverFlag(), rec.getAttritionOverFlag())) &&
                (Objects.isNull(dto.getAttritionRateFrom()) || rec.getAttritionChanceDifference().compareTo(dto.getAttritionRateFrom()) > 0) &&
                (Objects.isNull(dto.getAttritionRateTo()) || rec.getAttritionChanceDifference().compareTo(dto.getAttritionRateTo()) < 0)).collect(Collectors.toList());
        AtomicInteger seqGen = new AtomicInteger(0);
        list.forEach(rec -> rec.setSequenceNum(seqGen.incrementAndGet()));
        return list;
    }

    private List<HmeCosAttritionSumDTO> substituteMaterialGet(Long tenantId, List<HmeCosAttritionSumDTO> list, Map<String, BigDecimal> assembleQtyMap) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<HmeCosAttritionSumDTO> result = new ArrayList<>();
        List<BomComponentWorkcellVO> workcellVOList = list.stream().map(representation -> {
            BomComponentWorkcellVO bomComponent = new BomComponentWorkcellVO();
            bomComponent.setWorkOrderId(representation.getWorkOrderId());
            bomComponent.setBomComponentId(representation.getBomComponentId());
            bomComponent.setWorkcellId(representation.getWorkcellId());
            return bomComponent;
        }).collect(Collectors.toList());
        List<List<BomComponentWorkcellVO>> splitSqlList = CommonUtils.splitSqlList(workcellVOList, 2000);
        List<WorkOrderAttritionSumRepresentationDTO> substituteAllList = new ArrayList<>();
        for (List<BomComponentWorkcellVO> voList : splitSqlList) {
            // 查询替代
            List<WorkOrderAttritionSumRepresentationDTO> dtoList = workOrderAttritionSumMapper.selectSubstituteList(tenantId, voList);
            if (CollectionUtils.isNotEmpty(dtoList)) {
                substituteAllList.addAll(dtoList);
            }
        }
        // 分组汇总
        Map<String, List<WorkOrderAttritionSumRepresentationDTO>> substituteAllMap = substituteAllList.stream().collect(Collectors.groupingBy(dto -> this.splitStr(dto)));
        List<WorkOrderAttritionSumRepresentationDTO> groupSubstituteAllList = new ArrayList<>();
        substituteAllMap.entrySet().forEach(substituteMap -> {
            List<WorkOrderAttritionSumRepresentationDTO> valueList = substituteMap.getValue();
            WorkOrderAttritionSumRepresentationDTO dto = new WorkOrderAttritionSumRepresentationDTO();
            BeanUtils.copyProperties(valueList.get(0), dto);
            Double assemblyQuantity = valueList.stream().map(WorkOrderAttritionSumRepresentationDTO::getAssemblyQuantity).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            Double materialNcScrapQty = valueList.stream().map(WorkOrderAttritionSumRepresentationDTO::getMaterialNcScrapQty).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            dto.setAssemblyQuantity(assemblyQuantity != null ? BigDecimal.valueOf(assemblyQuantity) : BigDecimal.ZERO);
            dto.setMaterialNcScrapQty(materialNcScrapQty != null ? BigDecimal.valueOf(materialNcScrapQty) : BigDecimal.ZERO);
            groupSubstituteAllList.add(dto);
        });

        if (CollectionUtils.isNotEmpty(groupSubstituteAllList)) {
            Map<String, Map<String, List<WorkOrderAttritionSumRepresentationDTO>>> substituteMap = groupSubstituteAllList.stream().collect(Collectors.groupingBy(WorkOrderAttritionSumRepresentationDTO::getWorkOrderId, Collectors.groupingBy(WorkOrderAttritionSumRepresentationDTO::getSourceBomComponentId)));
            Set<String> collectionSet = new HashSet<>();
            for (HmeCosAttritionSumDTO component : list) {
                Map<String, List<WorkOrderAttritionSumRepresentationDTO>> bomComponentSubMap = substituteMap.get(component.getWorkOrderId());
                if (bomComponentSubMap != null && !bomComponentSubMap.isEmpty()) {
                    // 分类替代料
                    List<WorkOrderAttritionSumRepresentationDTO> substituteList = bomComponentSubMap.get(component.getBomComponentId());
                    // 如果有替代料需要对替代料计算
                    if (CollectionUtils.isNotEmpty(substituteList)) {
                        if (!collectionSet.contains(component.getWorkOrderId() + "," + component.getBomComponentId())) {
                            this.substituteCombination(result, collectionSet, component, substituteList, assembleQtyMap);
                        }
                    } else {
                        result.add(componentQuantityCalculate(component));
                    }
                }
            }
        } else {
            for (HmeCosAttritionSumDTO component : list) {
                result.add(componentQuantityCalculate(component));
            }
        }
        return result;
    }

    private String splitStr (WorkOrderAttritionSumRepresentationDTO dto) {
        StringBuffer sb = new StringBuffer();
        sb.append(dto.getWorkOrderId());
        sb.append(dto.getBomComponentId());
        sb.append(dto.getComponentMaterialId());
        sb.append(dto.getDemandQuantity());
        sb.append(dto.getAttritionChance());
        sb.append(dto.getAttritionLimit());
        sb.append(dto.getMaterialNcScrapQty());
        sb.append(dto.getBomSubstituteGroup());
        sb.append(dto.getSourceType());
        return sb.toString();
    }

    private void substituteCombination(List<HmeCosAttritionSumDTO> list, Set<String> collectionSet, HmeCosAttritionSumDTO component, List<WorkOrderAttritionSumRepresentationDTO> substituteList, Map<String, BigDecimal> assembleQtyMap) {
        // 计算实物报损数量
        BeanCopier copier = BeanCopier.create(WorkOrderAttritionSumRepresentationDTO.class, HmeCosAttritionSumDTO.class, false);
        List<HmeCosAttritionSumDTO> substituteNewList = substituteList.stream().map(sub -> {
            HmeCosAttritionSumDTO dto = new HmeCosAttritionSumDTO();
            copier.copy(sub, dto, null);
            BigDecimal assembleQty = assembleQtyMap.getOrDefault(this.spliceStr(new HmeCosAttritionSumVO() {{
                setWorkOrderId(sub.getWorkOrderId());
                setMaterialId(sub.getComponentMaterialId());
            }}), BigDecimal.ZERO);
            dto.setProcessNcScrapQty(assembleQty != null ? assembleQty : BigDecimal.ZERO);
            dto.setScrappedQuantity(assembleQty.add(dto.getMaterialNcScrapQty()));
            return dto;
        }).collect(Collectors.toList());
        List<HmeCosAttritionSumDTO> bomSubstituteList = substituteNewList.stream().filter(rec -> "BOM_SUBSTITUTE".equals(rec.getSourceType())).collect(Collectors.toList());
        List<HmeCosAttritionSumDTO> globalSubstituteList = substituteNewList.stream().filter(rec -> "GLOBAL_SUBSTITUTE".equals(rec.getSourceType())).collect(Collectors.toList());
        // 根据是否有替代料回写替代组
        component.setBomSubstituteGroup(CollectionUtils.isNotEmpty(bomSubstituteList) ? bomSubstituteList.get(0).getBomSubstituteGroup() : null);
        component.setGlobalSubstituteGroup(CollectionUtils.isNotEmpty(globalSubstituteList) ? globalSubstituteList.get(0).getGlobalSubstituteGroup() : null);
        // 20210727 modify by sanfeng.zhang for wenxin.zhang 报损数量改为材料不良报损 实物报损数量=材料不良报废+工序不良报废
        component.setScrappedQuantity(component.getMaterialNcScrapQty().add(component.getProcessNcScrapQty()));
        // 计算计划内/外报损
        scrappedQuantityCalculate(component);
        // 计算合计
        BigDecimal materialScrappedSumQuantity = component.getScrappedQuantity().add(substituteNewList.stream().map(HmeCosAttritionSumDTO::getScrappedQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal assemblyQtySum = component.getAssemblyQuantity().add(substituteNewList.stream().map(HmeCosAttritionSumDTO::getAssemblyQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal materialAttritionChance = assemblyQtySum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : materialScrappedSumQuantity.divide(assemblyQtySum, 6, BigDecimal.ROUND_HALF_UP).multiply(PERCENT).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal attritionChanceDifference = materialAttritionChance.subtract(component.getAttritionChance());

        // 回写计算结果
        component.setMaterialAttritionChance(materialAttritionChance);
        component.setAttritionChanceDifference(attritionChanceDifference);
        component.setAttritionOverFlag(attritionChanceDifference.compareTo(BigDecimal.ZERO) > 0 ? YES : NO);

        // 整合数据，先插入组件和bom替代按照行号插入，然后是全局替代料
        list.add(component);
        list.addAll(bomSubstituteList.stream().map(rec -> scrappedQuantityCalculate(this.substituteCompletion(rec, component))).collect(Collectors.toList()));
        list.addAll(globalSubstituteList.stream().map(rec -> scrappedQuantityCalculate(this.substituteCompletion(rec, component))).collect(Collectors.toList()));
        // 收集过的行不再收集
        collectionSet.add(component.getWorkOrderId() + "," + component.getBomComponentId());
        collectionSet.addAll(bomSubstituteList.stream().map(rec -> rec.getWorkOrderId() + "," + rec.getBomComponentId()).collect(Collectors.toSet()));
    }

    public HmeCosAttritionSumDTO substituteCompletion(HmeCosAttritionSumDTO substitute, HmeCosAttritionSumDTO component) {
        if (Objects.isNull(component)) {
            return substitute;
        }
        // 工单相关字段
        substitute.setWorkOrderNum(component.getWorkOrderNum());
        substitute.setWorkOrderStatus(component.getWorkOrderStatus());
        substitute.setProductionVersion(component.getProductionVersion());
        substitute.setProductionVersionDescription(component.getProductionVersionDescription());
        substitute.setWorkOrderType(component.getWorkOrderType());
        substitute.setAssemblyMaterialId(component.getAssemblyMaterialId());
        substitute.setAssemblyMaterialCode(component.getAssemblyMaterialCode());
        substitute.setAssemblyMaterialName(component.getAssemblyMaterialName());
        substitute.setWoQuantity(component.getWoQuantity());
        substitute.setCompletedQuantity(component.getCompletedQuantity());
        substitute.setProdLineId(component.getProdLineId());
        substitute.setProdLineName(component.getProdLineName());
        substitute.setWorkcellId(component.getWorkcellId());
        substitute.setWorkcellName(component.getWorkcellName());
        substitute.setProcessId(component.getProcessId());
        // 合计字段
        substitute.setAttritionChanceDifference(component.getAttritionChanceDifference());
        substitute.setAttritionOverFlag(component.getAttritionOverFlag());
        substitute.setMaterialAttritionChance(component.getMaterialAttritionChance());
        return substitute;
    }

    private HmeCosAttritionSumDTO scrappedQuantityCalculate(HmeCosAttritionSumDTO component) {
        // 计算数量
        component.setPlannedScrappedQuantity(component.getScrappedQuantity().compareTo(component.getAttritionLimit()) <= 0 ? component.getScrappedQuantity() : component.getAttritionLimit());
        component.setUnplannedScrappedQuantity(component.getScrappedQuantity().compareTo(component.getAttritionLimit()) <= 0 ? BigDecimal.ZERO : component.getScrappedQuantity().subtract(component.getAttritionLimit()));
        return component;
    }

    private HmeCosAttritionSumDTO componentQuantityCalculate(HmeCosAttritionSumDTO component) {
        component.setScrappedQuantity(component.getMaterialNcScrapQty().add(component.getProcessNcScrapQty()));
        // 回写计算结果
        scrappedQuantityCalculate(component);

        BigDecimal materialAttritionChance = component.getAssemblyQuantity().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : component.getScrappedQuantity().divide(component.getAssemblyQuantity(), 6, BigDecimal.ROUND_HALF_UP).multiply(PERCENT).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal attritionChanceDifference = materialAttritionChance.subtract(component.getAttritionChance());
        component.setMaterialAttritionChance(materialAttritionChance);
        component.setAttritionChanceDifference(attritionChanceDifference);
        component.setAttritionOverFlag(attritionChanceDifference.compareTo(BigDecimal.ZERO) > 0 ? YES : NO);
        return component;
    }

    private List<HmeCosAttritionSumDTO> workcellRelationGet(Long tenantId, List<HmeCosAttritionSumDTO> list, WorkOrderAttritionSumQueryDTO dto) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 根据组件行获取工段信息，并判断是否有工段筛选条件，执行筛选
        List<List<HmeCosAttritionSumDTO>> splitSqlList = CommonUtils.splitSqlList(list, SQL_ITEM_COUNT_LIMIT);
        List<BomComponentWorkcellVO> bomComponentWorkcellVOList = new ArrayList<>();
        for (List<HmeCosAttritionSumDTO> hmeCosAttritionSumDTOS : splitSqlList) {
            List<BomComponentWorkcellVO> workcellVOList = workOrderAttritionSumMapper.selectBomComponentStationList2(tenantId, hmeCosAttritionSumDTOS);
            if (CollectionUtils.isNotEmpty(workcellVOList)) {
                bomComponentWorkcellVOList.addAll(workcellVOList);
            }
        }
        // 数据去重
        bomComponentWorkcellVOList = bomComponentWorkcellVOList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(s -> s.getWorkOrderId()+s.getBomComponentId()+s.getProdLineId()))), ArrayList::new));
        Map<String, List<BomComponentWorkcellVO>> lineWorkcellMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(bomComponentWorkcellVOList)) {
            List<BomComponentWorkcellVO> lineWorkcelllList = new ArrayList<>();
            List<List<BomComponentWorkcellVO>> splitBomComponentList = CommonUtils.splitSqlList(bomComponentWorkcellVOList, 2000);
            for (List<BomComponentWorkcellVO> voList : splitBomComponentList) {
                List<BomComponentWorkcellVO> bomComponentWorkcellVOS = hmeCosAttritionSumMapper.queryLineWorkcellByBomComponentIdAndProdLineId(tenantId, voList);
                if (CollectionUtils.isNotEmpty(bomComponentWorkcellVOS)) {
                    lineWorkcelllList.addAll(bomComponentWorkcellVOS);
                }
            }
            lineWorkcellMap = lineWorkcelllList.stream().sorted(Comparator.comparing(BomComponentWorkcellVO::getPriority)).collect(Collectors.groupingBy(wkc -> this.spliceWkcStr(wkc), LinkedHashMap::new, Collectors.toList()));
        }
        Map<String, List<BomComponentWorkcellVO>> finalLineWorkcellMap = lineWorkcellMap;
        list.forEach(rec -> {
            List<BomComponentWorkcellVO> workcellVOList = finalLineWorkcellMap.getOrDefault(rec.getBomComponentId() + "_" + rec.getProdLineId(), Collections.EMPTY_LIST);
            if (CollectionUtils.isNotEmpty(workcellVOList)) {
                BomComponentWorkcellVO workcell = workcellVOList.get(0);
                rec.setProcessId(workcell.getProcessId());
                rec.setWorkcellId(workcell.getWorkcellId());
                rec.setWorkcellName(workcell.getWorkcellName());
            }
        });
        if (CollectionUtils.isNotEmpty(dto.getWorkcellIdSet())) {
            list = list.stream().filter(a -> dto.getWorkcellIdSet().contains(a.getWorkcellId())).collect(Collectors.toList());
        }
        return list;
    }

    private String spliceWkcStr(BomComponentWorkcellVO wkc) {
        StringBuffer sb = new StringBuffer();
        sb.append(wkc.getBomComponentId());
        sb.append("_");
        sb.append(wkc.getProdLineId());
        return sb.toString();
    }
}
