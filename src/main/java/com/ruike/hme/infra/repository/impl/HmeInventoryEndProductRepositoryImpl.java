package com.ruike.hme.infra.repository.impl;

import com.ruike.hme.domain.repository.HmeInventoryEndProductRepository;
import com.ruike.hme.domain.vo.*;
import com.ruike.hme.infra.constant.Constant;
import com.ruike.hme.infra.mapper.HmeInventoryEndProductMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.annotation.ProcessLovValue;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 售后在制品盘点-成品报表
 *
 * @author sanfeng.zhang@hand-china.com 2021/4/1 15:26
 */
@Component
public class HmeInventoryEndProductRepositoryImpl implements HmeInventoryEndProductRepository {

    @Autowired
    private HmeInventoryEndProductMapper hmeInventoryEndProductMapper;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @ProcessLovValue
    public Page<HmeInventoryEndProductVO2> inventoryEndProductQuery(Long tenantId, HmeInventoryEndProductVO dto, PageRequest pageRequest) {
        Page<HmeInventoryEndProductVO2> pageObj = PageHelper.doPage(pageRequest, () -> hmeInventoryEndProductMapper.inventoryEndProductQuery(tenantId, dto));
        // 半成品数量
        List<String> parentIdList = pageObj.getContent().stream().map(HmeInventoryEndProductVO2::getSplitRecordId).collect(Collectors.toList());
        Map<String, List<HmeInventoryEndProductVO4>> recordQtyMap = new HashMap<>();
        Map<String, List<HmeInventoryEndProductVO3>> workcellMap = new HashMap<>();
        Map<String, List<HmeInventoryEndProductVO2>> subRecordMap = new HashMap<>();
        Map<String, List<HmeInventoryEndProductVO5>> docNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(parentIdList)) {
            List<HmeInventoryEndProductVO4> recordQtyList = hmeInventoryEndProductMapper.queryQtyByParentRecord(tenantId, parentIdList);
            recordQtyMap = recordQtyList.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO4::getSplitRecordId));

            // 批量获取工位
            List<HmeInventoryEndProductVO3> workcellList = hmeInventoryEndProductMapper.batchQuerySplitWorkcell(tenantId, parentIdList, dto.getWorkcellCodeList());
            if (CollectionUtils.isNotEmpty(workcellList)) {
                workcellMap = workcellList.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO3::getSplitRecordId));
            }
            // 获取顶层下子层信息
            List<HmeInventoryEndProductVO2> subRecordList = this.querySubRecordList(tenantId, dto, parentIdList);
            if (CollectionUtils.isNotEmpty(subRecordList)) {
                subRecordMap = subRecordList.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO2::getTopSplitRecordId, LinkedHashMap::new, Collectors.toList()));
            }
            List<String> topMaterialLotCodeList = pageObj.getContent().stream().map(HmeInventoryEndProductVO2::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> subMaterialLotCodeList = subRecordList.stream().map(HmeInventoryEndProductVO2::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> allMaterialLotCodeList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(topMaterialLotCodeList)) {
                allMaterialLotCodeList.addAll(topMaterialLotCodeList);
            }
            if (CollectionUtils.isNotEmpty(subMaterialLotCodeList)) {
                allMaterialLotCodeList.addAll(subMaterialLotCodeList);
            }
            // 根据条码批量获取入库单号
            if (CollectionUtils.isNotEmpty(allMaterialLotCodeList)) {
                List<HmeInventoryEndProductVO5> hmeInventoryEndProductVO5s = hmeInventoryEndProductMapper.queryDocNumByMaterialLotCodeList(tenantId, allMaterialLotCodeList);
                docNumMap = hmeInventoryEndProductVO5s.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO5::getMaterialLotId, LinkedHashMap::new, Collectors.toList()));
            }
        }
        // 值集 产品不支持嵌套 手动赋值
        List<LovValueDTO> splitStatusList = lovAdapter.queryLovValue("HME.SPLIT_STATUS", tenantId);
        List<LovValueDTO> flagYnList = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
        List<LovValueDTO> codeStatusList = lovAdapter.queryLovValue("MT.MTLOT.STATUS", tenantId);

        Map<String, List<HmeInventoryEndProductVO5>> finalDocNumMap = docNumMap;
        for (HmeInventoryEndProductVO2 hmeInventoryEndProductVO2 : pageObj.getContent()) {
            List<HmeInventoryEndProductVO4> qtyList = recordQtyMap.get(hmeInventoryEndProductVO2.getSplitRecordId());
            // 半成品数量
            if (CollectionUtils.isNotEmpty(qtyList)) {
                hmeInventoryEndProductVO2.setQty(qtyList.get(0).getSubQty());
            }
            // 工位
            List<HmeInventoryEndProductVO3> workcells = workcellMap.get(hmeInventoryEndProductVO2.getSplitRecordId());
            if (CollectionUtils.isNotEmpty(workcells)) {
                hmeInventoryEndProductVO2.setWorkcellCode(workcells.get(0).getWorkcellCode());
                hmeInventoryEndProductVO2.setWorkcellName(workcells.get(0).getWorkcellName());
            }
            // 入库单号
            List<HmeInventoryEndProductVO5> topDocNumList = finalDocNumMap.get(hmeInventoryEndProductVO2.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(topDocNumList)) {
                hmeInventoryEndProductVO2.setInstructionDocNum(topDocNumList.get(0).getInstructionDocNum());
                hmeInventoryEndProductVO2.setDocNumFlag(Constant.ConstantValue.NO);
            } else {
                hmeInventoryEndProductVO2.setDocNumFlag(Constant.ConstantValue.YES);
            }

            // 获取子层记录
            List<HmeInventoryEndProductVO2> subProductRecordList = subRecordMap.get(hmeInventoryEndProductVO2.getSplitRecordId());
            // 展开标识 若父层半成品数量和查询的数量不一致 则展开  否则不展开
            if (CollectionUtils.isNotEmpty(subProductRecordList)) {
                // 插入来源SN
                subProductRecordList.stream().map(sub -> {
                    // 入库单号
                    List<HmeInventoryEndProductVO5> subDocNumList = finalDocNumMap.get(sub.getMaterialLotId());
                    if (CollectionUtils.isNotEmpty(subDocNumList)) {
                        sub.setInstructionDocNum(subDocNumList.get(0).getInstructionDocNum());
                        sub.setDocNumFlag(Constant.ConstantValue.NO);
                    } else {
                        sub.setDocNumFlag(Constant.ConstantValue.YES);
                    }

                    sub.setUnfoldFlag(true);
                    if (StringUtils.isNotBlank(sub.getSplitStatus())) {
                        Optional<LovValueDTO> filterOpt = splitStatusList.stream().filter(lov -> StringUtils.equals(lov.getValue(), sub.getSplitStatus())).findFirst();
                        if (filterOpt.isPresent()) {
                            sub.setSplitStatusMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(sub.getEnableFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(lov -> StringUtils.equals(lov.getValue(), sub.getEnableFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            sub.setEnableFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(sub.getMfFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getMfFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            sub.setMfFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(sub.getMaterialLotCodeStatus())) {
                        Optional<LovValueDTO> filterOpt = codeStatusList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getMaterialLotCodeStatus())).findFirst();
                        if (filterOpt.isPresent()) {
                            sub.setMaterialLotCodeStatusMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(sub.getCreateSnFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getCreateSnFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            sub.setCreateSnFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(sub.getDocNumFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getDocNumFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            sub.setDocNumFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    return sub;
                });
                if (hmeInventoryEndProductVO2.getQty() != null) {
                    if (hmeInventoryEndProductVO2.getQty().compareTo(BigDecimal.valueOf(subProductRecordList.size())) == 0) {
                        hmeInventoryEndProductVO2.setUnfoldFlag(false);
                    } else {
                        hmeInventoryEndProductVO2.setUnfoldFlag(true);
                    }
                }
                hmeInventoryEndProductVO2.setChildList(subProductRecordList);
            }
        }
        return pageObj;
    }

    private List<HmeInventoryEndProductVO2> querySubRecordList(Long tenantId, HmeInventoryEndProductVO dto, List<String> parentIdList) {
        List<HmeInventoryEndProductVO2> hmeInventoryEndProductVO2s = hmeInventoryEndProductMapper.batchQuerySubRecord(tenantId, dto, parentIdList);
        List<LovValueDTO> splitStatusList = lovAdapter.queryLovValue("HME.SPLIT_STATUS", tenantId);
        List<LovValueDTO> flagList = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
        List<LovValueDTO> statusList = lovAdapter.queryLovValue("MT.MTLOT.STATUS", tenantId);
        for (HmeInventoryEndProductVO2 vo2 : hmeInventoryEndProductVO2s) {
            // 当前状态
            if (StringUtils.isNotBlank(vo2.getSplitStatus())) {
                Optional<LovValueDTO> firstOpt = splitStatusList.stream().filter(vo -> StringUtils.equals(vo.getValue(), vo2.getSplitStatus())).findFirst();
                vo2.setSplitStatusMeaning(firstOpt.isPresent() ? firstOpt.get().getMeaning() : "");
            }
            // 有效性
            if (StringUtils.isNotBlank(vo2.getEnableFlag())) {
                Optional<LovValueDTO> firstOpt = flagList.stream().filter(vo -> StringUtils.equals(vo.getValue(), vo2.getEnableFlag())).findFirst();
                vo2.setEnableFlagMeaning(firstOpt.isPresent() ? firstOpt.get().getMeaning() : "");
            }
            // 在制标识
            if (StringUtils.isNotBlank(vo2.getMfFlag())) {
                Optional<LovValueDTO> firstOpt = flagList.stream().filter(vo -> StringUtils.equals(vo.getValue(), vo2.getMfFlag())).findFirst();
                vo2.setMfFlagMeaning(firstOpt.isPresent() ? firstOpt.get().getMeaning() : "");
            }
            // 条码状态
            if (StringUtils.isNotBlank(vo2.getMaterialLotCodeStatus())) {
                Optional<LovValueDTO> firstOpt = statusList.stream().filter(vo -> StringUtils.equals(vo.getValue(), vo2.getMaterialLotCodeStatus())).findFirst();
                vo2.setMaterialLotCodeStatusMeaning(firstOpt.isPresent() ? firstOpt.get().getMeaning() : "");
            }
            // 是否创建SN
            if (StringUtils.isNotBlank(vo2.getCreateSnFlag())) {
                Optional<LovValueDTO> firstOpt = flagList.stream().filter(vo -> StringUtils.equals(vo.getValue(), vo2.getCreateSnFlag())).findFirst();
                vo2.setCreateSnFlagMeaning(firstOpt.isPresent() ? firstOpt.get().getMeaning() : "");
            }
            // 入库单是否为空
            if (StringUtils.isNotBlank(vo2.getDocNumFlag())) {
                Optional<LovValueDTO> firstOpt = flagList.stream().filter(vo -> StringUtils.equals(vo.getValue(), vo2.getDocNumFlag())).findFirst();
                vo2.setDocNumFlagMeaning(firstOpt.isPresent() ? firstOpt.get().getMeaning() : "");
            }
        }
        return hmeInventoryEndProductVO2s;
    }

    @Override
    @ProcessLovValue
    public List<HmeInventoryEndProductVO3> inventoryEndProductExport(Long tenantId, HmeInventoryEndProductVO dto) {
        List<HmeInventoryEndProductVO2> topInventoryEndProductList = hmeInventoryEndProductMapper.inventoryEndProductQuery(tenantId, dto);
        // 半成品数量
        List<String> parentIdList = topInventoryEndProductList.stream().map(HmeInventoryEndProductVO2::getSplitRecordId).collect(Collectors.toList());
        Map<String, List<HmeInventoryEndProductVO4>> recordQtyMap = new HashMap<>();
        Map<String, List<HmeInventoryEndProductVO3>> workcellMap = new HashMap<>();
        Map<String, List<HmeInventoryEndProductVO2>> subRecordMap = new HashMap<>();
        Map<String, List<HmeInventoryEndProductVO5>> docNumMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(parentIdList)) {
            List<HmeInventoryEndProductVO4> recordQtyList = hmeInventoryEndProductMapper.queryQtyByParentRecord(tenantId, parentIdList);
            recordQtyMap = recordQtyList.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO4::getSplitRecordId));

            // 批量获取工位
            List<HmeInventoryEndProductVO3> workcellList = hmeInventoryEndProductMapper.batchQuerySplitWorkcell(tenantId, parentIdList, dto.getWorkcellCodeList());
            if (CollectionUtils.isNotEmpty(workcellList)) {
                workcellMap = workcellList.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO3::getSplitRecordId));
            }
            // 获取顶层下子层信息
            List<HmeInventoryEndProductVO2> subRecordList = this.querySubRecordList(tenantId, dto, parentIdList);
            if (CollectionUtils.isNotEmpty(subRecordList)) {
                subRecordMap = subRecordList.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO2::getTopSplitRecordId, LinkedHashMap::new, Collectors.toList()));
            }
            List<String> topMaterialLotCodeList = topInventoryEndProductList.stream().map(HmeInventoryEndProductVO2::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> subMaterialLotCodeList = subRecordList.stream().map(HmeInventoryEndProductVO2::getMaterialLotId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<String> allMaterialLotCodeList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(topMaterialLotCodeList)) {
                allMaterialLotCodeList.addAll(topMaterialLotCodeList);
            }
            if (CollectionUtils.isNotEmpty(subMaterialLotCodeList)) {
                allMaterialLotCodeList.addAll(subMaterialLotCodeList);
            }
            // 根据条码批量获取入库单号
            if (CollectionUtils.isNotEmpty(allMaterialLotCodeList)) {
                List<HmeInventoryEndProductVO5> hmeInventoryEndProductVO5s = hmeInventoryEndProductMapper.queryDocNumByMaterialLotCodeList(tenantId, allMaterialLotCodeList);
                docNumMap = hmeInventoryEndProductVO5s.stream().collect(Collectors.groupingBy(HmeInventoryEndProductVO5::getMaterialLotId, LinkedHashMap::new, Collectors.toList()));
            }

        }
        Map<String, List<HmeInventoryEndProductVO5>> finalDocNumMap = docNumMap;
        Map<String, List<HmeInventoryEndProductVO4>> finalRecordQtyMap = recordQtyMap;
        Map<String, List<HmeInventoryEndProductVO3>> finalWorkcellMap =workcellMap;
        Map<String, List<HmeInventoryEndProductVO2>> finalSubRecordMap = subRecordMap;
        List<HmeInventoryEndProductVO3> resultList = new ArrayList<>();

        // 值集 产品不支持嵌套 手动赋值
        List<LovValueDTO> splitStatusList = lovAdapter.queryLovValue("HME.SPLIT_STATUS", tenantId);
        List<LovValueDTO> flagYnList = lovAdapter.queryLovValue("WMS.FLAG_YN", tenantId);
        List<LovValueDTO> codeStatusList = lovAdapter.queryLovValue("MT.MTLOT.STATUS", tenantId);
        topInventoryEndProductList.forEach(vo -> {
            List<HmeInventoryEndProductVO4> qtyList = finalRecordQtyMap.get(vo.getSplitRecordId());
            // 半成品数量
            if (CollectionUtils.isNotEmpty(qtyList)) {
                vo.setQty(qtyList.get(0).getSubQty());
            }
            // 工位
            List<HmeInventoryEndProductVO3> workcells = finalWorkcellMap.get(vo.getSplitRecordId());
            if (CollectionUtils.isNotEmpty(workcells)) {
                vo.setWorkcellCode(workcells.get(0).getWorkcellCode());
                vo.setWorkcellName(workcells.get(0).getWorkcellName());
            }
            // 入库单号
            List<HmeInventoryEndProductVO5> topDocNumList = finalDocNumMap.get(vo.getMaterialLotId());
            if (CollectionUtils.isNotEmpty(topDocNumList)) {
                vo.setInstructionDocNum(topDocNumList.get(0).getInstructionDocNum());
                vo.setDocNumFlag(Constant.ConstantValue.NO);
            } else {
                vo.setDocNumFlag(Constant.ConstantValue.YES);
            }

            HmeInventoryEndProductVO3 vo3 = new HmeInventoryEndProductVO3();
            BeanUtils.copyProperties(vo, vo3);
            resultList.add(vo3);

            // 获取子层记录
            List<HmeInventoryEndProductVO2> subProductRecordList = finalSubRecordMap.get(vo.getSplitRecordId());
            // 展开标识 若父层半成品数量和查询的数量不一致 则展开  否则不展开
            if (CollectionUtils.isNotEmpty(subProductRecordList)) {
                // 插入来源SN
                subProductRecordList.stream().forEach(sub -> {
                    // 入库单号
                    List<HmeInventoryEndProductVO5> subDocNumList = finalDocNumMap.get(sub.getMaterialLotId());
                    if (CollectionUtils.isNotEmpty(subDocNumList)) {
                        sub.setInstructionDocNum(subDocNumList.get(0).getInstructionDocNum());
                        sub.setDocNumFlag(Constant.ConstantValue.NO);
                    } else {
                        sub.setDocNumFlag(Constant.ConstantValue.YES);
                    }
                    HmeInventoryEndProductVO3 subVo = new HmeInventoryEndProductVO3();
                    BeanUtils.copyProperties(sub, subVo);
                    if (StringUtils.isNotBlank(subVo.getSplitStatus())) {
                        Optional<LovValueDTO> filterOpt = splitStatusList.stream().filter(lov -> StringUtils.equals(lov.getValue(), sub.getSplitStatus())).findFirst();
                        if (filterOpt.isPresent()) {
                            subVo.setSplitStatusMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(subVo.getEnableFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(lov -> StringUtils.equals(lov.getValue(), sub.getEnableFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            subVo.setEnableFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(subVo.getMfFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getMfFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            subVo.setMfFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(subVo.getMaterialLotCodeStatus())) {
                        Optional<LovValueDTO> filterOpt = codeStatusList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getMaterialLotCodeStatus())).findFirst();
                        if (filterOpt.isPresent()) {
                            subVo.setMaterialLotCodeStatusMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(subVo.getCreateSnFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getCreateSnFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            subVo.setCreateSnFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    if (StringUtils.isNotBlank(subVo.getDocNumFlag())) {
                        Optional<LovValueDTO> filterOpt = flagYnList.stream().filter(ss -> StringUtils.equals(ss.getValue(), sub.getDocNumFlag())).findFirst();
                        if (filterOpt.isPresent()) {
                            subVo.setDocNumFlagMeaning(filterOpt.get().getMeaning());
                        }
                    }
                    resultList.add(subVo);

                });
            }
        });
        return resultList;
    }
}
