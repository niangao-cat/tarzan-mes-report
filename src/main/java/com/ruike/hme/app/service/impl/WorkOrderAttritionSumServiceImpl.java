package com.ruike.hme.app.service.impl;

import com.ruike.hme.api.dto.HmeCosAttritionSumDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumQueryDTO;
import com.ruike.hme.api.dto.WorkOrderAttritionSumRepresentationDTO;
import com.ruike.hme.api.dto.WorkOrderSubstituteGroupDTO;
import com.ruike.hme.app.assembler.WorkOrderAttritionSumAssembler;
import com.ruike.hme.app.service.WorkOrderAttritionSumService;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.mapper.WorkOrderAttritionSumMapper;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.hme.infra.util.StringCommonUtils;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.export.vo.ExportParam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import utils.Utils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruike.hme.infra.constant.Constant.ConstantValue.*;

/**
 * <p>
 * 工单损耗汇总报表 服务实现
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/12 16:17
 */
@Service
public class WorkOrderAttritionSumServiceImpl implements WorkOrderAttritionSumService {
    private final WorkOrderAttritionSumMapper mapper;
    private final WorkOrderAttritionSumAssembler assembler;

    private final static BigDecimal PERCENT = new BigDecimal("100");

    public WorkOrderAttritionSumServiceImpl(WorkOrderAttritionSumMapper mapper, WorkOrderAttritionSumAssembler assembler) {
        this.mapper = mapper;
        this.assembler = assembler;
    }

    @Override
    @ProcessLovValue
    public Page<WorkOrderAttritionSumRepresentationDTO> page(Long tenantId, WorkOrderAttritionSumQueryDTO dto, PageRequest pageRequest) {
        List<WorkOrderAttritionSumRepresentationDTO> list = mapper.selectList(tenantId, dto);
        if (CollectionUtils.isNotEmpty(list)) {
            list = representationComplete(tenantId, list, dto);
        }
        Page<WorkOrderAttritionSumRepresentationDTO> page = Utils.pagedList(pageRequest.getPage(), pageRequest.getSize(), list);
        this.pendingNcQuantityCalculate(tenantId, page.getContent());
        return page;
    }

    private Map<String, BigDecimal> processNcScrapGet(Long tenantId, List<WorkOrderAttritionSumRepresentationDTO> list, WorkOrderAttritionSumQueryDTO dto) {
        List<String> workOrderIds = list.stream().map(WorkOrderAttritionSumRepresentationDTO::getWorkOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<String, BigDecimal> assembleQtyMap = new HashMap<>();
        List<String> eoIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workOrderIds)) {
            List<List<String>> woIdList = CommonUtils.splitSqlList(workOrderIds, 2000);
            for (List<String> woIds : woIdList) {
                List<String> eoIds = mapper.processNcEoListGet(tenantId, woIds);
                if (CollectionUtils.isNotEmpty(eoIds)) {
                    eoIdList.addAll(eoIds);
                }
            }
            // 去重
            eoIdList = eoIdList.stream().distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(eoIdList)) {
                List<HmeWorkOrderAttritionSumVO> assembleQtyResultList = new ArrayList<>();
                // 拆分eo 对拆分后的数据再进行分组 避免eo 过大查询慢
                List<List<String>> splitEoList = CommonUtils.splitSqlList(eoIdList, 1000);
                for (List<String> eoIds : splitEoList) {
                    List<HmeWorkOrderAttritionSumVO> splitResult = mapper.processNcScrapGet(tenantId, eoIds);
                    // 去重 全局替代料时会有重复
                    splitResult = splitResult.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(s -> s.getEoComponentActualId()+s.getWorkOrderId()+s.getMaterialId()+s.getBomComponentId()))), ArrayList::new));
                    if (CollectionUtils.isNotEmpty(splitResult)) {
                        assembleQtyResultList.addAll(splitResult);
                    }
                }
                assembleQtyMap = assembleQtyResultList.stream().collect(Collectors.toMap(rs -> this.spliceStr(rs), HmeWorkOrderAttritionSumVO::getAssembleQty, (oldVal, currVal) -> oldVal.add(currVal)));
            }
        }
        for (WorkOrderAttritionSumRepresentationDTO vo : list) {
            StringBuffer sb = new StringBuffer();
            sb.append(vo.getWorkOrderId());
            sb.append(vo.getComponentMaterialId());
            sb.append(StringUtils.isBlank(vo.getBomComponentId()) ? "" : vo.getBomComponentId());
            BigDecimal processNcScrapQty = assembleQtyMap.get(sb.toString());
            vo.setProcessNcScrapQty(processNcScrapQty != null ? processNcScrapQty : BigDecimal.ZERO);
        }
        return assembleQtyMap;
    }

    private String spliceStr(HmeWorkOrderAttritionSumVO vo) {
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getWorkOrderId());
        sb.append(vo.getMaterialId());
        sb.append(vo.getBomComponentId());
        return sb.toString();
    }

    private List<WorkOrderAttritionSumRepresentationDTO> representationComplete(Long tenantId, List<WorkOrderAttritionSumRepresentationDTO> list, WorkOrderAttritionSumQueryDTO dto) {
        // 获取工段相关
        list = this.workcellRelationGet(tenantId, list, dto);

        // 工序不良报废
        Map<String, BigDecimal> assembleQtyMap = this.processNcScrapGet(tenantId, list, dto);

        // 计算联产品损耗
        this.coproductScrappedCalculate(tenantId, list);

        // 整合替代料
        list = this.substituteMaterialGet(tenantId, list, assembleQtyMap);

        //获取同一替代组装配数量
        Map<String,List<WorkOrderAttritionSumRepresentationDTO>> quantityMap = list.stream().filter(rec-> Objects.nonNull(rec.getBomSubstituteGroup()) || Objects.nonNull(rec.getGlobalSubstituteGroup()))
                .collect(Collectors.groupingBy(t-> t.getWorkOrderNum() + "-" + (t.getBomSubstituteGroup()==null?"":t.getBomSubstituteGroup()) + (t.getGlobalSubstituteGroup()==null?"":t.getGlobalSubstituteGroup())));
        List<WorkOrderAttritionSumRepresentationDTO> defaultList = new ArrayList<>();

        // 根据结果筛选
        list = list.stream().filter(rec -> (StringUtils.isBlank(dto.getScrappedOverFlag()) || StringCommonUtils.equalsIgnoreBlank(dto.getScrappedOverFlag(), rec.getAttritionOverFlag())) &&
                (Objects.isNull(dto.getAttritionRateFrom()) || rec.getAttritionChanceDifference().compareTo(dto.getAttritionRateFrom()) > 0) &&
                (Objects.isNull(dto.getAttritionRateTo()) || rec.getAttritionChanceDifference().compareTo(dto.getAttritionRateTo()) < 0) &&
                // 工单损耗报表展示逻辑修改 By 田欣 20210916
                // 1、全局替代组和BOM替代组均为空时，只显示装配数量＞0的行信息
                // 2、全局替代组或BOM替代组不为空时，替代组内只要有一种物料装配数量＞0，整组行信息均显示
                ((Objects.isNull(rec.getBomSubstituteGroup()) && Objects.isNull(rec.getGlobalSubstituteGroup()) && rec.getAssemblyQuantity().compareTo(BigDecimal.ZERO) > 0)||
                        ((Objects.nonNull(rec.getBomSubstituteGroup()) || Objects.nonNull(rec.getGlobalSubstituteGroup())) &&
                                CollectionUtils.isNotEmpty(quantityMap.getOrDefault(rec.getWorkOrderNum() + "-" + (rec.getBomSubstituteGroup()==null?"":rec.getBomSubstituteGroup()) + (rec.getGlobalSubstituteGroup()==null?"":rec.getGlobalSubstituteGroup()), defaultList)
                                        .stream().filter(t->t.getAssemblyQuantity().compareTo(BigDecimal.ZERO) > 0).collect(Collectors.toList()))))
        ).collect(Collectors.toList());
        AtomicInteger seqGen = new AtomicInteger(0);

        //获取替代组中物料的主物料
        List<String> mainCodeList = list.stream().filter(t-> Objects.nonNull(t.getBomSubstituteGroup()) || Objects.nonNull(t.getGlobalSubstituteGroup()))
                .map(WorkOrderAttritionSumRepresentationDTO::getComponentMaterialCode).collect(Collectors.toList());
        List<WorkOrderSubstituteGroupDTO> substituteGroupDTOS = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(mainCodeList)){

            //SQL中IN语句后列表太大，改为分批查询 By 田欣 2021-10-08
//            substituteGroupDTOS = mapper.querySubstituteGroup(tenantId,mainCodeList);
            List<List<String>> splitSqlList = CommonUtils.splitSqlList(mainCodeList, 1000);
            for (List<String> materialCodeList : splitSqlList) {
                List<WorkOrderSubstituteGroupDTO> dtoList = mapper.querySubstituteGroup(tenantId,materialCodeList);
                if (CollectionUtils.isNotEmpty(dtoList)) {
                    substituteGroupDTOS.addAll(dtoList);
                }
            }
        }
        Map<String,List<WorkOrderSubstituteGroupDTO>> substituteGroupMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(substituteGroupDTOS)){
            substituteGroupMap = substituteGroupDTOS.stream().collect(Collectors.groupingBy(t-> t.getSubstituteGroup() + "-" + t.getMaterialCode()));
        }
        Map<String, List<WorkOrderSubstituteGroupDTO>> finalSubstituteGroupMap = substituteGroupMap;
        list.forEach(rec -> {
            rec.setSequenceNum(seqGen.incrementAndGet());
            // 3、全局替代组或BOM替代组不为空时，计划内报损合计、计划外报损合计、报损合计、实物损耗率(%)、实际损耗率(%)、损耗率差异(%)列，在主料行显示。
            if(Objects.nonNull(rec.getBomSubstituteGroup()) || Objects.nonNull(rec.getGlobalSubstituteGroup())){
                List<WorkOrderSubstituteGroupDTO> list1 = finalSubstituteGroupMap.get((rec.getBomSubstituteGroup()==null?"":rec.getBomSubstituteGroup()) + (rec.getGlobalSubstituteGroup()==null?"":rec.getGlobalSubstituteGroup()) + "-" + rec.getComponentMaterialCode());
                if (CollectionUtils.isNotEmpty(list1) && !rec.getComponentMaterialCode().equals(list1.get(0).getMainMaterialCode())){
                    rec.setPlannedScrappedSumQuantity(BigDecimal.ZERO);
                    rec.setUnplannedScrappedSumQuantity(BigDecimal.ZERO);
                    rec.setScrappedSumQuantity(BigDecimal.ZERO);
                    rec.setMaterialAttritionChance(BigDecimal.ZERO);
                    rec.setActualAttritionChance(BigDecimal.ZERO);
                    rec.setAttritionChanceDifference(BigDecimal.ZERO);
                    rec.setAttritionOverFlag("N");
                }
            }
        });
        return list;
    }

    private void coproductScrappedCalculate(Long tenantId, List<WorkOrderAttritionSumRepresentationDTO> list) {
        List<String> workOrderIds = list.stream().map(WorkOrderAttritionSumRepresentationDTO::getWorkOrderId).distinct().collect(Collectors.toList());
        List<WorkOrderQtyVO> coproductScrappedList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workOrderIds)) {
            // 拆分 过大查询慢
            List<List<String>> splitWorkOrderIdList = CommonUtils.splitSqlList(workOrderIds, 2000);
            for (List<String> woIds : splitWorkOrderIdList) {
                List<WorkOrderQtyVO> workOrderQtyVOList = mapper.selectCoproductScrappedList(tenantId, woIds);
                if (CollectionUtils.isNotEmpty(workOrderQtyVOList)) {
                    coproductScrappedList.addAll(workOrderQtyVOList);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(coproductScrappedList)) {
            Map<String, BigDecimal> coproductScrappedMap = coproductScrappedList.stream().collect(Collectors.toMap(WorkOrderQtyVO::getWorkOrderId, WorkOrderQtyVO::getQuantity, (a, b) -> a));
            list.forEach(rec -> rec.setCoproductScrappedQuantity(coproductScrappedMap.getOrDefault(rec.getWorkOrderId(), BigDecimal.ZERO).multiply(rec.getUsageQty())));
        } else {
            list.forEach(rec -> rec.setCoproductScrappedQuantity(BigDecimal.ZERO));
        }
    }

    private List<WorkOrderAttritionSumRepresentationDTO> workcellRelationGet(Long tenantId, List<WorkOrderAttritionSumRepresentationDTO> list, WorkOrderAttritionSumQueryDTO dto) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 根据组件行获取工段信息，并判断是否有工段筛选条件，执行筛选
        List<List<WorkOrderAttritionSumRepresentationDTO>> splitSqlList = CommonUtils.splitSqlList(list, SQL_ITEM_COUNT_LIMIT);
        List<BomComponentWorkcellVO> bomComponentWorkcellVOList = new ArrayList<>();
        for (List<WorkOrderAttritionSumRepresentationDTO> sumRepresentationDTOList : splitSqlList) {
            List<BomComponentWorkcellVO> workcellVOList = mapper.selectBomComponentStationList(tenantId, sumRepresentationDTOList);
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
                List<BomComponentWorkcellVO> bomComponentWorkcellVOS = mapper.queryLineWorkcellByBomComponentIdAndProdLineId(tenantId, voList);
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

    private List<WorkOrderAttritionSumRepresentationDTO> substituteMaterialGet(Long tenantId, List<WorkOrderAttritionSumRepresentationDTO> list, Map<String, BigDecimal> assembleQtyMap) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        List<WorkOrderAttritionSumRepresentationDTO> result = new ArrayList<>();

        List<BomComponentWorkcellVO> workcellVOList = list.stream().map(assembler::representationToBomComponent).collect(Collectors.toList());
        List<List<BomComponentWorkcellVO>> splitSqlList = CommonUtils.splitSqlList(workcellVOList, 2000);
        List<WorkOrderAttritionSumRepresentationDTO> substituteAllList = new ArrayList<>();
        for (List<BomComponentWorkcellVO> voList : splitSqlList) {
            // 查询替代
            List<WorkOrderAttritionSumRepresentationDTO> dtoList = mapper.selectSubstituteList(tenantId, voList);
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
            for (WorkOrderAttritionSumRepresentationDTO component : list) {
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
            for (WorkOrderAttritionSumRepresentationDTO component : list) {
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

    private WorkOrderAttritionSumRepresentationDTO componentQuantityCalculate(WorkOrderAttritionSumRepresentationDTO component) {
        // 20210727 modify by sanfeng.zhang for wenxin.zhang 报损数量改为材料不良报损 实物报损数量=材料不良报废+工序不良报废
        component.setScrappedQuantity(component.getMaterialNcScrapQty().add(component.getProcessNcScrapQty()));

        // 回写计算结果
        scrappedQuantityCalculate(component);
        component.setScrappedSumQuantity(component.getScrappedQuantity().add(component.getCoproductScrappedQuantity()));
        component.setPlannedScrappedSumQuantity(component.getScrappedSumQuantity().compareTo(component.getAttritionLimit()) <= 0 ? component.getScrappedSumQuantity() : component.getAttritionLimit());
        component.setUnplannedScrappedSumQuantity(component.getScrappedSumQuantity().compareTo(component.getAttritionLimit()) <= 0 ? BigDecimal.ZERO : component.getScrappedSumQuantity().subtract(component.getAttritionLimit()));

        BigDecimal materialAttritionChance = component.getAssemblyQuantity().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : component.getScrappedQuantity().divide(component.getAssemblyQuantity(), 6, BigDecimal.ROUND_HALF_UP).multiply(PERCENT).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal actualAttritionChance = component.getAssemblyQuantity().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : component.getScrappedSumQuantity().divide(component.getAssemblyQuantity(), 6, BigDecimal.ROUND_HALF_UP).multiply(PERCENT).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal attritionChanceDifference = actualAttritionChance.subtract(component.getAttritionChance());

        component.setMaterialAttritionChance(materialAttritionChance);
        component.setActualAttritionChance(actualAttritionChance);
        component.setAttritionChanceDifference(attritionChanceDifference);
        component.setAttritionOverFlag(attritionChanceDifference.compareTo(BigDecimal.ZERO) > 0 ? YES : NO);
        return component;
    }

    private void substituteCombination(List<WorkOrderAttritionSumRepresentationDTO> list, Set<String> collectionSet, WorkOrderAttritionSumRepresentationDTO component, List<WorkOrderAttritionSumRepresentationDTO> substituteList, Map<String, BigDecimal> assembleQtyMap) {
        // 计算实物报损数量
        substituteList = substituteList.stream().map(sub -> {
            String mapKey = sub.getWorkOrderId() + sub.getComponentMaterialId() + (StringUtils.isNotBlank(sub.getBomComponentId()) ? sub.getBomComponentId() : "");
            BigDecimal assembleQty = assembleQtyMap.getOrDefault(mapKey, BigDecimal.ZERO);
            sub.setProcessNcScrapQty(assembleQty != null ? assembleQty : BigDecimal.ZERO);
            sub.setScrappedQuantity(assembleQty.add(sub.getMaterialNcScrapQty()));
            return sub;
        }).collect(Collectors.toList());
        List<WorkOrderAttritionSumRepresentationDTO> bomSubstituteList = substituteList.stream().filter(rec -> "BOM_SUBSTITUTE".equals(rec.getSourceType())).collect(Collectors.toList());
        List<WorkOrderAttritionSumRepresentationDTO> globalSubstituteList = substituteList.stream().filter(rec -> "GLOBAL_SUBSTITUTE".equals(rec.getSourceType())).collect(Collectors.toList());
        // 根据是否有替代料回写替代组
        component.setBomSubstituteGroup(CollectionUtils.isNotEmpty(bomSubstituteList) ? bomSubstituteList.get(0).getBomSubstituteGroup() : null);
        component.setGlobalSubstituteGroup(CollectionUtils.isNotEmpty(globalSubstituteList) ? globalSubstituteList.get(0).getGlobalSubstituteGroup() : null);
        // 20210727 modify by sanfeng.zhang for wenxin.zhang 报损数量改为材料不良报损 实物报损数量=材料不良报废+工序不良报废
        component.setScrappedQuantity(component.getMaterialNcScrapQty().add(component.getProcessNcScrapQty()));
        // 计算计划内/外报损
        scrappedQuantityCalculate(component);
        // 计算合计
        BigDecimal materialScrappedSumQuantity = component.getScrappedQuantity().add(substituteList.stream().map(WorkOrderAttritionSumRepresentationDTO::getScrappedQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal scrappedSumQuantity = materialScrappedSumQuantity.add(component.getCoproductScrappedQuantity() != null ? component.getCoproductScrappedQuantity() : BigDecimal.ZERO);
        BigDecimal attritionLimitSum = component.getAttritionLimit().add(substituteList.stream().map(WorkOrderAttritionSumRepresentationDTO::getAttritionLimit).reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal assemblyQtySum = component.getAssemblyQuantity().add(substituteList.stream().map(WorkOrderAttritionSumRepresentationDTO::getAssemblyQuantity).reduce(BigDecimal.ZERO, BigDecimal::add));
        BigDecimal plannedScrappedSumQuantity = scrappedSumQuantity.compareTo(attritionLimitSum) <= 0 ? scrappedSumQuantity : attritionLimitSum;
        BigDecimal unplannedScrappedSumQuantity = scrappedSumQuantity.compareTo(attritionLimitSum) <= 0 ? BigDecimal.ZERO : scrappedSumQuantity.subtract(attritionLimitSum);
        BigDecimal materialAttritionChance = assemblyQtySum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : materialScrappedSumQuantity.divide(assemblyQtySum, 6, BigDecimal.ROUND_HALF_UP).multiply(PERCENT).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal actualAttritionChance = assemblyQtySum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : scrappedSumQuantity.divide(assemblyQtySum, 6, BigDecimal.ROUND_HALF_UP).multiply(PERCENT).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal attritionChanceDifference = actualAttritionChance.subtract(component.getAttritionChance());

        // 回写计算结果
        component.setPlannedScrappedSumQuantity(plannedScrappedSumQuantity);
        component.setUnplannedScrappedSumQuantity(unplannedScrappedSumQuantity);
        component.setScrappedSumQuantity(scrappedSumQuantity);
        component.setMaterialAttritionChance(materialAttritionChance);
        component.setActualAttritionChance(actualAttritionChance);
        component.setAttritionChanceDifference(attritionChanceDifference);
        component.setAttritionOverFlag(attritionChanceDifference.compareTo(BigDecimal.ZERO) > 0 ? YES : NO);

        // 整合数据，先插入组件和bom替代按照行号插入，然后是全局替代料
        list.add(component);
        list.addAll(bomSubstituteList.stream().map(rec -> scrappedQuantityCalculate(assembler.substituteCompletion(rec, component))).collect(Collectors.toList()));
        list.addAll(globalSubstituteList.stream().map(rec -> scrappedQuantityCalculate(assembler.substituteCompletion(rec, component))).collect(Collectors.toList()));
        // 收集过的行不再收集
        collectionSet.add(component.getWorkOrderId() + "," + component.getBomComponentId());
        collectionSet.addAll(bomSubstituteList.stream().map(rec -> rec.getWorkOrderId() + "," + rec.getBomComponentId()).collect(Collectors.toSet()));
    }

    private WorkOrderAttritionSumRepresentationDTO scrappedQuantityCalculate(WorkOrderAttritionSumRepresentationDTO component) {
        // 计算数量
        component.setPlannedScrappedQuantity(component.getScrappedQuantity().compareTo(component.getAttritionLimit()) <= 0 ? component.getScrappedQuantity() : component.getAttritionLimit());
        component.setUnplannedScrappedQuantity(component.getScrappedQuantity().compareTo(component.getAttritionLimit()) <= 0 ? BigDecimal.ZERO : component.getScrappedQuantity().subtract(component.getAttritionLimit()));
        return component;
    }
    private void pendingNcQuantityCalculate(Long tenantId, List<WorkOrderAttritionSumRepresentationDTO> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        List<PendingNcQueryVO> ncQueryVOList = list.stream().map(assembler::representationToNc).collect(Collectors.toList());
        List<List<PendingNcQueryVO>> splitSqlList = CommonUtils.splitSqlList(ncQueryVOList, SQL_ITEM_COUNT_LIMIT);
        List<PendingNcQueryVO> ncAllList = new ArrayList<>();
        for (List<PendingNcQueryVO> pendingNcQueryVOS : splitSqlList) {
            List<PendingNcQueryVO> ncList = mapper.selectPendingNcList(tenantId, pendingNcQueryVOS);
            if (CollectionUtils.isNotEmpty(ncList)) {
                ncAllList.addAll(ncList);
            }
        }
        Map<String, List<PendingNcQueryVO>> ncMap = ncAllList.stream().collect(Collectors.groupingBy(rec -> rec.getWorkOrderId() + "," + rec.getProcessId() + "," + rec.getMaterialId()));
        list.forEach(rec -> {
            String key = rec.getWorkOrderId() + "," + rec.getProcessId() + "," + rec.getComponentMaterialId();
            List<PendingNcQueryVO> ncList = ncMap.getOrDefault(key, Collections.EMPTY_LIST);
            Double quantity = ncList.stream().map(PendingNcQueryVO::getQuantity).mapToDouble(BigDecimal::doubleValue).summaryStatistics().getSum();
            rec.setPendingNcQuantity(quantity != null ? BigDecimal.valueOf(quantity) : BigDecimal.ZERO);
        });
    }

    @Override
    @ProcessLovValue
    public List<WorkOrderAttritionSumRepresentationDTO> export(Long tenantId, WorkOrderAttritionSumQueryDTO dto, ExportParam exportParam) {
        List<WorkOrderAttritionSumRepresentationDTO> list = mapper.selectList(tenantId, dto);
        if (CollectionUtils.isNotEmpty(list)) {
            list = representationComplete(tenantId, list, dto);
            this.pendingNcQuantityCalculate(tenantId, list);
        }
        return list;
    }
}
