package com.ruike.wms.infra.repository.impl;

import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.wms.api.dto.WmsProductionExecutionWholeProcessMonitoringReportDTO;
import com.ruike.wms.domain.repository.WmsProductionExecutionWholeProcessMonitoringReportRepository;
import com.ruike.wms.domain.vo.WmsProductionExecutionWholeProcessMonitoringReportVO;
import com.ruike.wms.infra.mapper.WmsProductionExecutionWholeProcessMonitoringReportMapper;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @description：生产执行全过程监控报表
 * @author ywj
 * @email wenjie.yang01@hand-china.com
 * @date 2021/2/19
 * @time 15:03
 * @version 0.0.1
 * @return
 */
@Component
public class WmsProductionExecutionWholeProcessMonitoringReportRepositoryImpl implements WmsProductionExecutionWholeProcessMonitoringReportRepository {

    @Autowired
    WmsProductionExecutionWholeProcessMonitoringReportMapper mapper;


    /**
     * @param tenantId
     * @param pageRequest
     * @param dto
     * @return io.choerodon.core.domain.Page<com.ruike.wms.domain.vo.WorkOrderInProcessDetailsQueryReportVO>
     * @description 工单在制明细查询报表
     * @author ywj
     * @email wenjie.yang01@hand-china.com
     * @date 2020/11/18
     * @time 15:31
     * @version 0.0.1
     */
    @Override
    public Page<WmsProductionExecutionWholeProcessMonitoringReportVO> list(Long tenantId, PageRequest pageRequest, WmsProductionExecutionWholeProcessMonitoringReportDTO dto) {
        if(StringUtils.isNotEmpty(dto.getItemMaterialAttr())){
            dto.setItemMaterialList(Arrays.asList(dto.getItemMaterialAttr().split(",")));
        }
        Page<WmsProductionExecutionWholeProcessMonitoringReportVO> result = PageHelper.doPage(pageRequest, () -> mapper.list(tenantId, dto));
        if(CollectionUtils.isNotEmpty(result.getContent())){
            displayFieldsCompletion(tenantId, result.getContent());
        }
        return result;
    }

    @Override
    public List<WmsProductionExecutionWholeProcessMonitoringReportVO> listExport(Long tenantId, WmsProductionExecutionWholeProcessMonitoringReportDTO dto) {
        if(StringUtils.isNotEmpty(dto.getItemMaterialAttr())){
            dto.setItemMaterialList(Arrays.asList(dto.getItemMaterialAttr().split(",")));
        }
        List<WmsProductionExecutionWholeProcessMonitoringReportVO> list = mapper.list(tenantId, dto);
        if(CollectionUtils.isNotEmpty(list)){
            displayFieldsCompletion(tenantId, list);
        }
        return list;
    }

    private void displayFieldsCompletion(Long tenantId, List<WmsProductionExecutionWholeProcessMonitoringReportVO> list){
        List<String> workOrderIdList = list.stream().map(WmsProductionExecutionWholeProcessMonitoringReportVO::getWorkOrderId).distinct().collect(Collectors.toList());
        //COS 工单
        List<String> cosWorkOrderIds = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(workOrderIdList)) {
            //查询COS工单ID
            cosWorkOrderIds = mapper.queryCosWorkOrderIdList(tenantId, workOrderIdList);
            if (CollectionUtils.isNotEmpty(cosWorkOrderIds)) {
                workOrderIdList.removeAll(cosWorkOrderIds);
            }
        }

        //对非COS工单进行处理
        Map<String, Long> woWipQtyMap = new HashMap<>();
        Map<String, Long> woAbandonQtyMap = new HashMap<>();
        Map<String, Long> woinstorkQtyMap = new HashMap<>();
        Map<String, Long> woNotInstorkQtyMap = new HashMap<>();
        if(CollectionUtils.isNotEmpty(workOrderIdList)){
            //根据工单ID批量查询工单在制数量、工单报废数量、工单已入库数量、工单未入库数量
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> woWipQtyList = new ArrayList<>();
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> woAbandonQtyList = new ArrayList<>();
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> woQtyList = new ArrayList<>();
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> woInstorkQtyList = new ArrayList<>();
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> woNotInstorkQtyList = new ArrayList<>();
            List<List<String>> splitWorkOrderIdList = CommonUtils.splitSqlList(workOrderIdList, 1000);
            for (List<String> split:splitWorkOrderIdList) {
                woWipQtyList.addAll(mapper.queryWipQtyByWoIdList(tenantId, split));
                woAbandonQtyList.addAll(mapper.queryAbandonQtyByWoIdList(tenantId, split));
                woQtyList.addAll(mapper.queryWoinstorkQtyByWoIdList(tenantId, split));
            }
            if(CollectionUtils.isNotEmpty(woWipQtyList)){
                woWipQtyMap = woWipQtyList.stream().collect(Collectors.toMap(WmsProductionExecutionWholeProcessMonitoringReportVO::getWorkOrderId, WmsProductionExecutionWholeProcessMonitoringReportVO::getWipQty));
            }
            if(CollectionUtils.isNotEmpty(woAbandonQtyList)){
                woAbandonQtyMap = woAbandonQtyList.stream().collect(Collectors.toMap(WmsProductionExecutionWholeProcessMonitoringReportVO::getWorkOrderId, WmsProductionExecutionWholeProcessMonitoringReportVO::getAbandonQty));
            }
            if(CollectionUtils.isNotEmpty(woQtyList)){
                woInstorkQtyList = woQtyList.stream().filter(item -> !"14".equals(item.getWarehouseType())).collect(Collectors.toList());
                woNotInstorkQtyList = woQtyList.stream().filter(item -> "14".equals(item.getWarehouseType())).collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(woInstorkQtyList)){
                woinstorkQtyMap = woInstorkQtyList.stream().collect(Collectors
                        .groupingBy(WmsProductionExecutionWholeProcessMonitoringReportVO::getWorkOrderId, Collectors.summingLong(
                                c -> Objects.isNull(c.getWoinstorkQty()) ? 0L : c.getWoinstorkQty())));
            }
            if(CollectionUtils.isNotEmpty(woNotInstorkQtyList)){
                woNotInstorkQtyMap = woNotInstorkQtyList.stream().collect(Collectors
                        .groupingBy(WmsProductionExecutionWholeProcessMonitoringReportVO::getWorkOrderId, Collectors.summingLong(
                                c -> Objects.isNull(c.getWoinstorkQty()) ? 0L : c.getWoinstorkQty())));
            }
        }
        //根据工单、产品物料查询产品待入库数量、产品入库数量
        Map<String, List<WmsProductionExecutionWholeProcessMonitoringReportVO>> woProMaterialMap = list.stream().collect(Collectors.groupingBy(item -> {
            return item.getWorkOrderId() + "," + item.getProMaterialId();
        }));
        List<WmsProductionExecutionWholeProcessMonitoringReportVO> woProMaterialList = new ArrayList<>();
        Map<String, Long> eoinstorkQtyMap = new HashMap<>();
        Map<String, Long> eoNotInstorkQtyMap = new HashMap<>();
        for (Map.Entry<String, List<WmsProductionExecutionWholeProcessMonitoringReportVO>> entry : woProMaterialMap.entrySet()) {
            String[] split = entry.getKey().split(",");
            if(split.length != 2){
                continue;
            }
            WmsProductionExecutionWholeProcessMonitoringReportVO singleWoProMaterial = new WmsProductionExecutionWholeProcessMonitoringReportVO();
            singleWoProMaterial.setWorkOrderId(split[0]);
            singleWoProMaterial.setProMaterialId(split[1]);
            woProMaterialList.add(singleWoProMaterial);
        }
        if(CollectionUtils.isNotEmpty(woProMaterialList)){
            List<List<WmsProductionExecutionWholeProcessMonitoringReportVO>> woProMaterialSplitList = CommonUtils.splitSqlList(woProMaterialList, 1000);
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> eoQtyList = new ArrayList<>();
            for (List<WmsProductionExecutionWholeProcessMonitoringReportVO> split:woProMaterialSplitList) {
                eoQtyList.addAll(mapper.queryEoinstorkQty(tenantId, split));
            }
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> eoInstorkQtyList = new ArrayList<>();
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> eoNotInstorkQtyList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(eoQtyList)){
                eoInstorkQtyList = eoQtyList.stream().filter(item -> !"14".equals(item.getWarehouseType())).collect(Collectors.toList());
                eoNotInstorkQtyList = eoQtyList.stream().filter(item -> "14".equals(item.getWarehouseType())).collect(Collectors.toList());
            }
            if(CollectionUtils.isNotEmpty(eoInstorkQtyList)){
                eoinstorkQtyMap = eoInstorkQtyList.stream().collect(Collectors
                        .groupingBy(item -> {
                            return item.getWorkOrderId() + "," + item.getProMaterialId();
                        }, Collectors.summingLong(
                                c -> Objects.isNull(c.getEotinstorkQty()) ? 0L : c.getEotinstorkQty())));
            }
            if(CollectionUtils.isNotEmpty(eoNotInstorkQtyList)){
                eoNotInstorkQtyMap = eoNotInstorkQtyList.stream().collect(Collectors
                        .groupingBy(item -> {
                            return item.getWorkOrderId() + "," + item.getProMaterialId();
                        }, Collectors.summingLong(
                                c -> Objects.isNull(c.getEotinstorkQty()) ? 0L : c.getEotinstorkQty())));
            }
        }

        //对COS工单进行处理
        Map<String, Long> cosReleasedQtyMap = new HashMap<>();
        Map<String, Long> cosAbandonQtyMap = new HashMap<>();

        if (CollectionUtils.isNotEmpty(cosWorkOrderIds)) {
            //查询 下达eo数量
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> cosReleasedQtyList = mapper.queryCosReleasedQtyByWorkOrderIds(tenantId, cosWorkOrderIds);
            if (CollectionUtils.isNotEmpty(cosReleasedQtyList)) {
                cosReleasedQtyMap = cosReleasedQtyList.stream().collect(Collectors.groupingBy(WmsProductionExecutionWholeProcessMonitoringReportVO::getWorkOrderId,
                        Collectors.summingLong(WmsProductionExecutionWholeProcessMonitoringReportVO :: getReleasedQty)));
            }

            //查询 工单报废数量
            List<WmsProductionExecutionWholeProcessMonitoringReportVO> cosAbandonQtyList = mapper.queryCosAbandonQtyByWorkOrderIds(tenantId,cosWorkOrderIds);
            if (CollectionUtils.isNotEmpty(cosAbandonQtyList)) {
                cosAbandonQtyMap =  cosAbandonQtyList.stream().collect(Collectors.toMap(WmsProductionExecutionWholeProcessMonitoringReportVO :: getWorkOrderId,
                        WmsProductionExecutionWholeProcessMonitoringReportVO :: getAbandonQty));
            }
        }

        //整合数据
        for (WmsProductionExecutionWholeProcessMonitoringReportVO result : list) {
            //cos 工单
            if (CollectionUtils.isNotEmpty(cosWorkOrderIds) && cosWorkOrderIds.contains(result.getWorkOrderId())) {
                result.setReleasedQty(cosReleasedQtyMap.getOrDefault(result.getWorkOrderId(), 0L));
                result.setAbandonQty(cosAbandonQtyMap.getOrDefault(result.getWorkOrderId(), 0L));
                // 工单完工数量
                if (Objects.isNull(result.getCompletedQty())) {
                    result.setCompletedQty(0L);
                }
                //工单在制数量
                Long wipQty = result.getReleasedQty() - result.getAbandonQty() - result.getCompletedQty();
                result.setWipQty(wipQty);
                //cos工单 工单待入库数量 等于 工单完工数量
                result.setWonotinstorkQty(result.getCompletedQty());
                result.setWoinstorkQty(0L);
                //COS工单产品物料编码 为 工单的物料编码
                result.setProMaterialCode(result.getMaterialCode());
                //是否主产品
                result.setFlag("是");
                //产品完工数量
                result.setCountQty(result.getCompletedQty());
                //产品入库数量
                result.setEonotinstorkQty(result.getCompletedQty());
                //产品待入库数量
                result.setEotinstorkQty(0L);
                //eo完工率
                if(result.getReleasedQty() == 0){
                    result.setEoCompleteRate("0.00%");
                }else {
                    BigDecimal eoCompleteRateBig = new BigDecimal(result.getCompletedQty()).divide(new BigDecimal(result.getReleasedQty()),4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal eoCompleteRate = eoCompleteRateBig.multiply(new BigDecimal("100").setScale(2, BigDecimal.ROUND_HALF_UP));
                    result.setEoCompleteRate(eoCompleteRate.toString() + "%");
                }
                //工单入库率
                if (result.getQty() == 0) {
                    result.setWonotinstorkRate("0.00%");
                } else {
                    BigDecimal wonotinstorkBig = new BigDecimal(result.getCompletedQty()).divide(new BigDecimal(result.getQty()), 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal wonotinstork = wonotinstorkBig.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    result.setWonotinstorkRate(wonotinstork.toString() + "%");
                }
                //产品完工率
                if (result.getQty() == 0) {
                    result.setCountQtyRate("0.00%");
                } else {
                    BigDecimal countQtyRateBig = new BigDecimal(result.getCountQty()).divide(new BigDecimal(result.getQty()), 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal countQtyRate = countQtyRateBig.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    result.setCountQtyRate(countQtyRate.toString() + "%");
                }
            }

            //非cos工单
            if (workOrderIdList.contains(result.getWorkOrderId())) {
                result.setWipQty(woWipQtyMap.getOrDefault(result.getWorkOrderId(), 0L));
                result.setAbandonQty(woAbandonQtyMap.getOrDefault(result.getWorkOrderId(), 0L));
                result.setWoinstorkQty(woinstorkQtyMap.getOrDefault(result.getWorkOrderId(), 0L));
                result.setWonotinstorkQty(woNotInstorkQtyMap.getOrDefault(result.getWorkOrderId(), 0L));
                //工单入库率
                if(result.getQty() == 0){
                    result.setWonotinstorkRate("0.00%");
                }else {
                    BigDecimal wonotinstorkBig = new BigDecimal(result.getWonotinstorkQty()).divide(new BigDecimal(result.getQty()), 4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal wonotinstork = wonotinstorkBig.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    result.setWonotinstorkRate(wonotinstork.toString() + "%");
                }
                if(StringUtils.isNotBlank(result.getWorkOrderId()) && StringUtils.isNotBlank(result.getProMaterialId())){
                    result.setEotinstorkQty(eoinstorkQtyMap.getOrDefault(result.getWorkOrderId() + "," + result.getProMaterialId(), 0L));
                    result.setEonotinstorkQty(eoNotInstorkQtyMap.getOrDefault(result.getWorkOrderId() + "," + result.getProMaterialId(), 0L));
                }
            }
        }
    }
}
